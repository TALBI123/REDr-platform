import { useState, useCallback } from 'react';

export const useFetch = (fetcher, immediate = true) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(immediate);
  const [error, setError] = useState(null);

  const execute = useCallback(async (...args) => {
    setLoading(true);
    setError(null);
    try {
      const result = await fetcher(...args);
      setData(result.data || result);
      return result.data || result;
    } catch (err) {
      const errorMsg = err.response?.data?.message || err.message || 'Erreur';
      setError(errorMsg);
      throw err;
    } finally {
      setLoading(false);
    }
  }, [fetcher]);

  return { data, loading, error, execute };
};
