import axios from 'axios';
import { PublicClientApplication } from '@azure/msal-browser';
import { msalConfig, tokenRequest } from '../authConfig';

// Reutilizamos una única instancia de MSAL para pedir tokens fuera de
// componentes React (acá, en el interceptor de Axios). msal-browser
// cachea internamente, así que crear la instancia de nuevo con la misma
// config es seguro y comparte el storage (sessionStorage) con la que usa
// MsalProvider en main.jsx.
const msalInstance = new PublicClientApplication(msalConfig);
let msalReady = null;
async function ensureMsalReady() {
  if (!msalReady) msalReady = msalInstance.initialize();
  return msalReady;
}

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(async (config) => {
  await ensureMsalReady();

  const account =
    msalInstance.getActiveAccount() ?? msalInstance.getAllAccounts()[0];

  if (account) {
    try {
      // acquireTokenSilent renueva el token con el refresh token cacheado
      // sin mostrar ningún popup/redirect mientras siga siendo válido.
      const result = await msalInstance.acquireTokenSilent({
        ...tokenRequest,
        account,
      });
      config.headers.Authorization = `Bearer ${result.accessToken}`;
    } catch (err) {
      // El silent falló (sesión expirada, etc.): mandamos al login en vez
      // de dejar salir el request sin token.
      console.warn('[api] No se pudo renovar el token en silencio:', err);
      await msalInstance.acquireTokenRedirect(tokenRequest);
    }
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // El Gateway rechazó el token (vencido/inválido): forzamos login de
      // nuevo contra Azure AD.
      msalInstance.acquireTokenRedirect(tokenRequest);
    }
    return Promise.reject(error);
  }
);

export const productosService = {
  listar: () => api.get('/productos'),
  obtener: (id) => api.get(`/productos/${id}`),
  marcas: () => api.get('/productos/marcas'),
  crear: (data) => api.post('/productos', data),
  actualizar: (id, data) => api.put(`/productos/${id}`, data),
  eliminar: (id) => api.delete(`/productos/${id}`),
};

export const bodegaService = {
  inventario: () => api.get('/bodega/inventario'),
  stock: (productoId) => api.get(`/bodega/inventario/${productoId}`),
  crearInventario: (data) => api.post('/bodega/inventario', data),
  agregarStock: (productoId, cantidad) =>
    api.patch(`/bodega/inventario/${productoId}/agregar`, { cantidad }),
  descontarStock: (productoId, cantidad) =>
    api.patch(`/bodega/inventario/${productoId}/descontar`, { cantidad }),
  alertas: () => api.get('/bodega/alertas'),
};

export default api;
