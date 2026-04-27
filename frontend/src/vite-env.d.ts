/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_AUTH_AUTHORITY: string;
  readonly VITE_AUTH_ISSUER: string;
  readonly VITE_AUTH_CLIENT_ID: string;
  readonly VITE_AUTH_REDIRECT_URI: string;
  readonly VITE_AUTH_LOGOUT_REDIRECT_URI: string;
  readonly VITE_AUTH_SCOPES: string;
  readonly VITE_AUTH_IDP_IDIR: string;
  readonly VITE_AUTH_IDP_BCEID: string;
  readonly VITE_API_BASE_URL: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
