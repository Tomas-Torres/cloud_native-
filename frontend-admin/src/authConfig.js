/**
 * Configuración de MSAL (Microsoft Authentication Library) para autenticar
 * el Front Admin contra Azure AD / Microsoft Entra ID, tal como lo pide el
 * diagrama del profe: Front Admin -> "MS Login" (Tenant App) -> JWT.
 *
 * VALORES A COMPLETAR (los da quien cree el App Registration en el
 * portal de Azure -> Microsoft Entra ID -> App registrations):
 *
 *   1. Crear un App Registration para el "Tenant App" (el que autentica).
 *      - Tipo de cuenta: normalmente "Single tenant" (solo tu organización/tenant).
 *      - Redirect URI: tipo SPA, apuntando a http://localhost:5174 (dev)
 *        y luego a la URL real una vez desplegado en Azure.
 *   2. En ese mismo App Registration -> "Expose an API":
 *      - Definir el Application ID URI (ej: api://<CLIENT_ID>)
 *      - Agregar un scope, ej: access_as_admin
 *      Este scope es lo que el Front Admin va a pedir para obtener un
 *      token que el backend (BFF/Gateway) pueda validar.
 *   3. Copiar Client ID y Tenant ID al archivo .env (ver .env.example).
 *
 * Nada de esto se hardcodea acá: todo sale de variables de entorno para
 * que cada integrante/entorno (dev, Azure) tenga su propia config.
 */

const clientId = import.meta.env.VITE_AZURE_CLIENT_ID;
const tenantId = import.meta.env.VITE_AZURE_TENANT_ID;
const redirectUri = import.meta.env.VITE_AZURE_REDIRECT_URI || window.location.origin;
// Application ID URI + scope expuesto en el App Registration (paso 2 de arriba).
// Ej: "api://00000000-0000-0000-0000-000000000000/access_as_admin"
const apiScope = import.meta.env.VITE_AZURE_API_SCOPE;

if (!clientId || !tenantId) {
  // Falla rápido y claro en desarrollo si falta configurar el .env,
  // en vez de dejar que MSAL tire un error críptico más adelante.
  console.error(
    '[authConfig] Faltan VITE_AZURE_CLIENT_ID y/o VITE_AZURE_TENANT_ID en el .env. ' +
      'Revisa frontend-admin/.env.example.'
  );
}

export const msalConfig = {
  auth: {
    clientId,
    authority: `https://login.microsoftonline.com/${tenantId}`,
    redirectUri,
    postLogoutRedirectUri: redirectUri,
  },
  cache: {
    // sessionStorage evita dejar el token pegado entre pestañas/usuarios
    // distintos en el mismo navegador (más seguro que localStorage para
    // un panel de admin).
    cacheLocation: 'sessionStorage',
    storeAuthStateInCookie: false,
  },
};

// Scopes que se piden al hacer login interactivo.
export const loginRequest = {
  scopes: apiScope ? [apiScope] : ['openid', 'profile'],
};

// Scopes que se piden al renovar el token silenciosamente (mismo scope
// de la API, para que el token que llega al Gateway sirva de una vez).
export const tokenRequest = {
  scopes: apiScope ? [apiScope] : ['openid', 'profile'],
};
