import { useMsal, useIsAuthenticated } from '@azure/msal-react';
import { Sun, Moon, Shield, LogOut } from 'lucide-react';
import { useTheme } from '../../context/ThemeContext';

function Navbar() {
  const { darkMode, toggleTheme } = useTheme();
  const { instance, accounts } = useMsal();
  const isAuthenticated = useIsAuthenticated();
  const account = accounts[0];

  const handleLogout = () => {
    instance.logoutRedirect();
  };

  return (
    <nav className="sticky top-0 z-50 bg-white/80 dark:bg-gray-900/80 backdrop-blur-md border-b border-gray-200 dark:border-gray-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center gap-2">
            <Shield className="w-8 h-8 text-admin-600" />
            <span className="text-xl font-bold bg-gradient-to-r from-admin-600 to-admin-400 bg-clip-text text-transparent">
              Lumina Admin
            </span>
          </div>

          <div className="flex items-center gap-3">
            <button
              onClick={toggleTheme}
              className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
              aria-label="Cambiar tema"
            >
              {darkMode ? <Sun className="w-5 h-5" /> : <Moon className="w-5 h-5" />}
            </button>

            {isAuthenticated && (
              <>
                <span className="hidden sm:block text-sm text-gray-500 dark:text-gray-400">
                  {account?.name || account?.username}
                </span>
                <button
                  onClick={handleLogout}
                  className="flex items-center gap-1.5 text-sm font-medium text-gray-600 dark:text-gray-300 hover:text-red-600 transition-colors"
                >
                  <LogOut className="w-4 h-4" />
                  Salir
                </button>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
