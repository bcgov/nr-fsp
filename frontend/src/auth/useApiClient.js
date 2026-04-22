import { useMemo } from 'react';
import useAuth from './useAuth';
import createApiClient from './apiClient';

/**
 * Returns an API client instance bound to the current user session.
 *
 * Usage:
 *   const api = useApiClient();
 *   const data = await api.get('/fsp/123');
 */
export default function useApiClient() {
  const { getAccessToken, logout } = useAuth();
  return useMemo(() => createApiClient(getAccessToken, logout), [getAccessToken, logout]);
}
