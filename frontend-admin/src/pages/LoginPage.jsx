import { useMsal, useIsAuthenticated } from '@azure/msal-react';
import { Navigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Shield } from 'lucide-react';
import { loginRequest } from '../authConfig';

function LoginPage() {
  const { instance } = useMsal();
  const isAuthenticated = useIsAuthenticated();

  if (isAuthenticated) {
    return <Navigate to="/admin" replace />;
  }

  const handleLogin = () => {
    // loginRedirect es más robusto que loginPopup (no lo bloquean los
    // popup blockers) y es lo recomendado por Microsoft para producción.
    instance.loginRedirect(loginRequest);
  };

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="card w-full max-w-md p-8 text-center"
      >
        <div className="mx-auto mb-4 w-14 h-14 rounded-full bg-admin-100 dark:bg-admin-900 flex items-center justify-center">
          <Shield className="w-7 h-7 text-admin-600 dark:text-admin-300" />
        </div>
        <h1 className="text-2xl font-bold mb-2">Lumina Admin</h1>
        <p className="text-gray-500 dark:text-gray-400 mb-6">
          Panel de administración. Inicia sesión con tu cuenta corporativa.
        </p>
        <button onClick={handleLogin} className="btn-primary w-full">
          Iniciar sesión con Microsoft
        </button>
      </motion.div>
    </div>
  );
}

export default LoginPage;
