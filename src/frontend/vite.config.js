import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  preview: {
    allowedHosts: ['lobster-app-ohxaa.ondigitalocean.app', '.ondigitalocean.app'],
    port: 8080,
    host: '0.0.0.0'
  },
  server: {
    allowedHosts: ['lobster-app-ohxaa.ondigitalocean.app', '.ondigitalocean.app']
  }

})
