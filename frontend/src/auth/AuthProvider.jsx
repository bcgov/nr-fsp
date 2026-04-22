import React, { createContext, useCallback, useEffect, useMemo, useState } from 'react';
import cfg from './AuthConfig';

// ── Helpers ────────────────────────────────────────────────────────

/** Generate a cryptographically random string for PKCE / state. */
function randomString(length = 64) {
  const bytes = crypto.getRandomValues(new Uint8Array(length));
  return Array.from(bytes, (b) => b.toString(16).padStart(2, '0')).join('');
}

/** SHA-256 hash → base64url (for PKCE code_challenge). */
async function sha256Base64Url(plain) {
  const encoded = new TextEncoder().encode(plain);
  const digest = await crypto.subtle.digest('SHA-256', encoded);
  return btoa(String.fromCharCode(...new Uint8Array(digest)))
    .replace(/\+/g, '-')
    .replace(/\//g, '_')
    .replace(/=+$/, '');
}

/** Decode the payload of a JWT (no verification — the API does that). */
function decodeJwtPayload(token) {
  try {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(atob(base64));
  } catch {
    return null;
  }
}

// ── Session storage keys (survive page refreshes, cleared on tab close) ──
const STORAGE_PREFIX       = 'fsp_auth_';
const KEY_ACCESS_TOKEN     = `${STORAGE_PREFIX}access_token`;
const KEY_REFRESH_TOKEN    = `${STORAGE_PREFIX}refresh_token`;
const KEY_ID_TOKEN         = `${STORAGE_PREFIX}id_token`;
const KEY_CODE_VERIFIER    = `${STORAGE_PREFIX}code_verifier`;
const KEY_STATE            = `${STORAGE_PREFIX}state`;

// ── Context ────────────────────────────────────────────────────────
export const AuthContext = createContext(null);

// ── Provider ───────────────────────────────────────────────────────
export default function AuthProvider({ children }) {
  const [accessToken, setAccessToken]   = useState(() => sessionStorage.getItem(KEY_ACCESS_TOKEN));
  const [refreshToken, setRefreshToken] = useState(() => sessionStorage.getItem(KEY_REFRESH_TOKEN));
  const [idToken, setIdToken]           = useState(() => sessionStorage.getItem(KEY_ID_TOKEN));
  const [user, setUser]                 = useState(() => {
    const stored = sessionStorage.getItem(KEY_ID_TOKEN);
    return stored ? decodeJwtPayload(stored) : null;
  });
  const [isLoading, setIsLoading] = useState(true);

  // ── Persist tokens to sessionStorage ─────────────────────────────
  const storeTokens = useCallback(({ access_token, refresh_token, id_token }) => {
    if (access_token) {
      sessionStorage.setItem(KEY_ACCESS_TOKEN, access_token);
      setAccessToken(access_token);
    }
    if (refresh_token) {
      sessionStorage.setItem(KEY_REFRESH_TOKEN, refresh_token);
      setRefreshToken(refresh_token);
    }
    if (id_token) {
      sessionStorage.setItem(KEY_ID_TOKEN, id_token);
      setIdToken(id_token);
      setUser(decodeJwtPayload(id_token));
    }
  }, []);

  const clearTokens = useCallback(() => {
    [KEY_ACCESS_TOKEN, KEY_REFRESH_TOKEN, KEY_ID_TOKEN, KEY_CODE_VERIFIER, KEY_STATE].forEach(
      (k) => sessionStorage.removeItem(k)
    );
    setAccessToken(null);
    setRefreshToken(null);
    setIdToken(null);
    setUser(null);
  }, []);

  // ── Token exchange (auth code → tokens) ──────────────────────────
  const exchangeCode = useCallback(
    async (code) => {
      const codeVerifier = sessionStorage.getItem(KEY_CODE_VERIFIER);
      if (!codeVerifier) throw new Error('Missing PKCE code_verifier');

      const body = new URLSearchParams({
        grant_type:    'authorization_code',
        client_id:     cfg.clientId,
        code,
        redirect_uri:  cfg.redirectUri,
        code_verifier: codeVerifier,
      });

      const res = await fetch(cfg.tokenEndpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body,
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(`Token exchange failed: ${res.status} – ${text}`);
      }

      const tokens = await res.json();
      storeTokens(tokens);
      sessionStorage.removeItem(KEY_CODE_VERIFIER);
      sessionStorage.removeItem(KEY_STATE);
      return tokens;
    },
    [storeTokens]
  );

  // ── Silent refresh ───────────────────────────────────────────────
  const refreshAccessToken = useCallback(async () => {
    const currentRefresh = sessionStorage.getItem(KEY_REFRESH_TOKEN);
    if (!currentRefresh) {
      clearTokens();
      return null;
    }

    try {
      const body = new URLSearchParams({
        grant_type:    'refresh_token',
        client_id:     cfg.clientId,
        refresh_token: currentRefresh,
      });

      const res = await fetch(cfg.tokenEndpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body,
      });

      if (!res.ok) {
        // Refresh token expired or revoked — force re-login
        clearTokens();
        return null;
      }

      const tokens = await res.json();
      storeTokens(tokens);
      return tokens.access_token;
    } catch {
      clearTokens();
      return null;
    }
  }, [clearTokens, storeTokens]);

  // ── Initiate login (redirect to Cognito) ─────────────────────────
  const login = useCallback(async () => {
    const codeVerifier  = randomString(64);
    const codeChallenge = await sha256Base64Url(codeVerifier);
    const state         = randomString(32);

    sessionStorage.setItem(KEY_CODE_VERIFIER, codeVerifier);
    sessionStorage.setItem(KEY_STATE, state);

    const params = new URLSearchParams({
      response_type:         'code',
      client_id:             cfg.clientId,
      redirect_uri:          cfg.redirectUri,
      scope:                 cfg.scopes,
      state,
      code_challenge:        codeChallenge,
      code_challenge_method: 'S256',
    });

    window.location.href = `${cfg.authorizeEndpoint}?${params}`;
  }, []);

  // ── Logout (redirect to Cognito logout endpoint) ─────────────────
  const logout = useCallback(() => {
    const currentIdToken = sessionStorage.getItem(KEY_ID_TOKEN);
    clearTokens();

    const params = new URLSearchParams({
      client_id:            cfg.clientId,
      logout_uri:           cfg.logoutRedirectUri,
    });

    // If we have an id_token, pass it so Cognito can end the session
    if (currentIdToken) {
      params.set('id_token_hint', currentIdToken);
    }

    window.location.href = `${cfg.logoutEndpoint}?${params}`;
  }, [clearTokens]);

  // ── Handle callback on mount ─────────────────────────────────────
  useEffect(() => {
    const handleCallback = async () => {
      const params = new URLSearchParams(window.location.search);
      const code   = params.get('code');
      const state  = params.get('state');
      const error  = params.get('error');

      // Callback error from Cognito
      if (error) {
        console.error('Auth callback error:', error, params.get('error_description'));
        clearTokens();
        window.history.replaceState({}, '', '/');
        setIsLoading(false);
        return;
      }

      // Returning from Cognito with an auth code
      if (code && window.location.pathname === '/auth/callback') {
        const expectedState = sessionStorage.getItem(KEY_STATE);
        if (state !== expectedState) {
          console.error('State mismatch — possible CSRF');
          clearTokens();
          window.history.replaceState({}, '', '/');
          setIsLoading(false);
          return;
        }

        try {
          await exchangeCode(code);
          // Clean URL and redirect to app root
          window.history.replaceState({}, '', '/welcome');
        } catch (err) {
          console.error('Token exchange error:', err);
          clearTokens();
          window.history.replaceState({}, '', '/');
        }

        setIsLoading(false);
        return;
      }

      // Already have tokens from sessionStorage — check if access token
      // is expired and try a one-time refresh so the user isn't kicked
      // out on a page reload after the access token has lapsed.
      const existingToken = sessionStorage.getItem(KEY_ACCESS_TOKEN);
      if (existingToken) {
        const payload = decodeJwtPayload(existingToken);
        if (!payload?.exp || payload.exp * 1000 <= Date.now()) {
          // Access token expired — try refreshing once
          await refreshAccessToken();
        }
      }

      setIsLoading(false);
    };

    handleCallback();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // ── Getter for current access token (refreshes if expired) ───────
  const getAccessToken = useCallback(async () => {
    const token = sessionStorage.getItem(KEY_ACCESS_TOKEN);
    if (!token) return null;

    const payload = decodeJwtPayload(token);
    // If more than 30 s of life left, use it
    if (payload?.exp && payload.exp * 1000 - Date.now() > 30_000) {
      return token;
    }

    // Otherwise refresh
    return refreshAccessToken();
  }, [refreshAccessToken]);

  // ── Context value ────────────────────────────────────────────────
  const value = useMemo(
    () => ({
      isAuthenticated: !!accessToken,
      isLoading,
      user,               // decoded id_token payload
      accessToken,
      idToken,
      login,
      logout,
      getAccessToken,     // async — always returns a valid token or null
    }),
    [accessToken, idToken, isLoading, user, login, logout, getAccessToken]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
