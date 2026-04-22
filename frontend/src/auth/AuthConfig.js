// ── Cognito / OIDC configuration ───────────────────────────────────
// All values are injected at build time via Vite env variables.
// In OpenShift, set these in the deployment ConfigMap / secret.

const cfg = {
  // Cognito User Pool domain  (e.g. https://your-pool.auth.ca-central-1.amazoncognito.com)
  authority: import.meta.env.VITE_AUTH_AUTHORITY,

  // Cognito App Client ID  (public client — no secret)
  clientId: import.meta.env.VITE_AUTH_CLIENT_ID,

  // Where Cognito redirects after login
  redirectUri: import.meta.env.VITE_AUTH_REDIRECT_URI || `${window.location.origin}/auth/callback`,

  // Where Cognito redirects after logout
  logoutRedirectUri: import.meta.env.VITE_AUTH_LOGOUT_REDIRECT_URI || window.location.origin,

  // Scopes requested from Cognito
  scopes: import.meta.env.VITE_AUTH_SCOPES || 'openid profile email',

  // Base URL of the Spring Boot API  (e.g. https://api.example.gov.bc.ca)
  apiBaseUrl: import.meta.env.VITE_API_BASE_URL || '/api',
};

// ── Derived OIDC endpoints ─────────────────────────────────────────
cfg.authorizeEndpoint = `${cfg.authority}/oauth2/authorize`;
cfg.tokenEndpoint     = `${cfg.authority}/oauth2/token`;
cfg.logoutEndpoint    = `${cfg.authority}/logout`;
cfg.jwksEndpoint      = `${cfg.authority}/.well-known/jwks.json`;

export default Object.freeze(cfg);
