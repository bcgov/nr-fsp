import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  build: {
    outDir: 'build',   // keep the same output dir so Dockerfile needs no changes
    sourcemap: false,
  },
  server: {
    port: 3000,
  },
});
