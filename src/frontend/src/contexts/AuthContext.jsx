import { createContext, useCallback, useEffect, useState } from 'react';
import { authService } from '../services/authService';

export const AuthContext = createContext();
const AUTH_USER_KEY = 'rentcar.auth.user';

const formatObjectError = (data) => {
  if (!data || typeof data !== 'object') return null;

  const directMessage = data.message || data.error || data.detail || data.title;
  if (directMessage) return directMessage;

  const validationErrors = data.errors || data.fieldErrors;
  if (Array.isArray(validationErrors)) {
    return validationErrors
      .map((item) => item.message || item.defaultMessage || item.msg || String(item))
      .filter(Boolean)
      .join(', ');
  }

  if (validationErrors && typeof validationErrors === 'object') {
    return Object.entries(validationErrors)
      .map(([field, value]) => `${field}: ${Array.isArray(value) ? value.join(', ') : value}`)
      .join(', ');
  }

  return null;
};

const getAuthErrorMessage = (err, fallback) => {
  const data = err.response?.data;

  if (typeof data === 'string' && data.trim()) return data;

  const objectMessage = formatObjectError(data);
  if (objectMessage) return objectMessage;

  if (err.response?.status) {
    return `${fallback} (HTTP ${err.response.status})`;
  }

  if (err.code === 'ERR_NETWORK') {
    return "Impossible de contacter l'API. Verifiez que le backend est en ligne et que CORS autorise cette adresse.";
  }

  return err.message || fallback;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    try {
      const storedUser = localStorage.getItem(AUTH_USER_KEY);
      return storedUser ? JSON.parse(storedUser) : null;
    } catch {
      return null;
    }
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const saveUser = useCallback((nextUser) => {
    setUser(nextUser);
    if (nextUser) {
      localStorage.setItem(AUTH_USER_KEY, JSON.stringify(nextUser));
    } else {
      localStorage.removeItem(AUTH_USER_KEY);
    }
  }, []);

  useEffect(() => {
    const restoreSession = async () => {
      const storedUser = localStorage.getItem(AUTH_USER_KEY);
      if (!storedUser) {
        setLoading(false);
        return;
      }

      try {
        const response = await authService.getCurrentUser();
        saveUser(response.data);
      } catch (err) {
        try {
          saveUser(JSON.parse(storedUser));
        } catch {
          saveUser(null);
        }
      } finally {
        setLoading(false);
      }
    };

    restoreSession();
  }, [saveUser]);

  const login = useCallback(async (email, password) => {
    setLoading(true);
    setError(null);
    try {
      const response = await authService.login(email, password);
      const loginUser = {
        email: response.data.email || email,
        role: response.data.role,
      };
      saveUser(loginUser);

      try {
        const currentUser = await authService.getCurrentUser();
        saveUser(currentUser.data);
      } catch {
        // The login is valid even if the profile cookie is not readable in local dev.
      }

      return response.data;
    } catch (err) {
      const errorMsg = getAuthErrorMessage(err, 'Erreur de connexion');
      setError(errorMsg);
      throw err;
    } finally {
      setLoading(false);
    }
  }, [saveUser]);

  const logout = useCallback(async () => {
    setLoading(true);
    try {
      await authService.logout();
      saveUser(null);
      setError(null);
    } catch (err) {
      setError(getAuthErrorMessage(err, 'Erreur de deconnexion'));
      throw err;
    } finally {
      setLoading(false);
    }
  }, [saveUser]);

  const registerClient = useCallback(async (data) => {
    setLoading(true);
    setError(null);
    try {
      const response = await authService.registerClient(data);
      return response.data;
    } catch (err) {
      const errorMsg = getAuthErrorMessage(err, "Erreur d'inscription");
      setError(errorMsg);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const registerAgency = useCallback(async (data) => {
    setLoading(true);
    setError(null);
    try {
      const response = await authService.registerAgency(data);
      return response.data;
    } catch (err) {
      const errorMsg = getAuthErrorMessage(err, "Erreur d'inscription agence");
      setError(errorMsg);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const value = {
    user,
    loading,
    error,
    login,
    logout,
    registerClient,
    registerAgency,
    isAuthenticated: !!user,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
