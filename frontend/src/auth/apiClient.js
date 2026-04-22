import cfg from './AuthConfig';

// ── API Client ─────────────────────────────────────────────────────
// A thin fetch wrapper for the Spring Boot API.
//
// Usage:
//   const api = createApiClient(getAccessToken, logout);
//   const data = await api.get('/fsp/123');
//   await api.post('/fsp', { body: payload });
//   await api.put('/fsp/123', { body: payload });
//   await api.delete('/fsp/123');
//
// - Automatically attaches the Bearer token.
// - On a 401, attempts one silent refresh and retries.
// - If the retry also fails, calls logout() to force re-authentication.

export default function createApiClient(getAccessToken, logout) {
  /**
   * Core request function.
   * @param {string} path   — relative to VITE_API_BASE_URL (e.g. '/fsp/123')
   * @param {object} opts   — { method, body, headers, ...fetchOptions }
   * @param {boolean} isRetry — internal flag to prevent infinite retry loops
   */
  async function request(path, opts = {}, isRetry = false) {
    const { body, headers: extraHeaders, ...fetchOpts } = opts;

    const token = await getAccessToken();
    if (!token) {
      logout();
      throw new Error('No valid access token');
    }

    const url = `${cfg.apiBaseUrl}${path}`;

    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
      ...extraHeaders,
    };

    const res = await fetch(url, {
      ...fetchOpts,
      headers,
      body: body != null ? JSON.stringify(body) : undefined,
    });

    // ── 401 → attempt one silent refresh + retry ───────────────────
    if (res.status === 401 && !isRetry) {
      const freshToken = await getAccessToken();
      if (freshToken) {
        return request(path, opts, true);
      }
      logout();
      throw new Error('Session expired');
    }

    if (!res.ok) {
      const errorBody = await res.text().catch(() => '');
      const err = new Error(`API ${res.status}: ${errorBody}`);
      err.status = res.status;
      err.body = errorBody;
      throw err;
    }

    // 204 No Content
    if (res.status === 204) return null;

    return res.json();
  }

  return {
    get:    (path, opts = {}) => request(path, { ...opts, method: 'GET' }),
    post:   (path, opts = {}) => request(path, { ...opts, method: 'POST' }),
    put:    (path, opts = {}) => request(path, { ...opts, method: 'PUT' }),
    patch:  (path, opts = {}) => request(path, { ...opts, method: 'PATCH' }),
    delete: (path, opts = {}) => request(path, { ...opts, method: 'DELETE' }),
    request,
  };
}
