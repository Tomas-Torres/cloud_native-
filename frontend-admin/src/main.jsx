import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { PublicClientApplication, EventType } from '@azure/msal-browser';
import { MsalProvider } from '@azure/msal-react';
import App from './App';
import { ThemeProvider } from './context/ThemeContext';
import { msalConfig } from './authConfig';
import './index.css';

const msalInstance = new PublicClientApplication(msalConfig);

// Si ya hay una cuenta autenticada (recarga de página), la dejamos como
// cuenta activa para que useMsal()/useAccount() la encuentren de inmediato.
if (
  !msalInstance.getActiveAccount() &&
  msalInstance.getAllAccounts().length > 0
) {
  msalInstance.setActiveAccount(msalInstance.getAllAccounts()[0]);
}

msalInstance.addEventCallback((event) => {
  if (
    (event.eventType === EventType.LOGIN_SUCCESS ||
      event.eventType === EventType.ACQUIRE_TOKEN_SUCCESS) &&
    event.payload?.account
  ) {
    msalInstance.setActiveAccount(event.payload.account);
  }
});

async function bootstrap() {
  // Requerido por MSAL v3 antes de usar la instancia (procesa el redirect
  // de vuelta desde Azure AD si corresponde).
  await msalInstance.initialize();
  await msalInstance.handleRedirectPromise().catch((err) => {
    console.error('[MSAL] Error procesando el redirect de login:', err);
  });

  ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
      <MsalProvider instance={msalInstance}>
        <BrowserRouter>
          <ThemeProvider>
            <App />
          </ThemeProvider>
        </BrowserRouter>
      </MsalProvider>
    </React.StrictMode>
  );
}

bootstrap();
