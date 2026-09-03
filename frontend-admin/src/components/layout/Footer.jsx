import { Shield } from 'lucide-react';

function Footer() {
  return (
    <footer className="bg-white dark:bg-gray-900 border-t border-gray-200 dark:border-gray-800 mt-auto">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex flex-col md:flex-row items-center justify-between gap-4">
          <div className="flex items-center gap-2">
            <Shield className="w-6 h-6 text-admin-600" />
            <span className="font-bold text-admin-600">Lumina Admin</span>
          </div>
          <p className="text-sm text-gray-500 dark:text-gray-400">
            &copy; {new Date().getFullYear()} Tienda Retail Lumina. Panel de administración.
          </p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
