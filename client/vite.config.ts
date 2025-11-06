import path from 'path';
import { fileURLToPath } from 'url';

import react from '@vitejs/plugin-react-swc';
import { defineConfig } from 'vite';
import tailwindcss from '@tailwindcss/vite';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const srcPath = path.resolve(__dirname, 'src');

export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      '@': srcPath,
      '@api': path.resolve(srcPath, 'api'),
      '@api/*': path.resolve(srcPath, 'api', '*'),
      '@components': path.resolve(srcPath, 'components'),
      '@components/*': path.resolve(srcPath, 'components', '*'),
      '@schemas': path.resolve(srcPath, 'schemas'),
      '@schemas/*': path.resolve(srcPath, 'schemas', '*'),
      '@pages': path.resolve(srcPath, 'pages'),
      '@pages/*': path.resolve(srcPath, 'pages', '*'),
      '@utils': path.resolve(srcPath, 'utils'),
      '@utils/*': path.resolve(srcPath, 'utils', '*'),
      '@routes': path.resolve(srcPath, 'routes'),
      '@routes/*': path.resolve(srcPath, 'routes', '*'),
      '@theme': path.resolve(srcPath, 'theme'),
      '@theme/*': path.resolve(srcPath, 'theme', '*'),
    },
  },
  build: {
    outDir: 'dist',
    sourcemap: true,
    rollupOptions: {
      output: {
        manualChunks: (id) => {
          if (id.includes('node_modules')) {
            if (id.includes('react') || id.includes('react-dom') || id.includes('react-router')) {
              return 'vendor-react';
            }
            if (id.includes('@chakra-ui')) {
              return 'vendor-ui';
            }
            if (id.includes('axios')) {
              return 'vendor-http';
            }
            return 'vendor';
          }
        },
        chunkFileNames: 'js/[name]-[hash].js',
        entryFileNames: 'js/[name]-[hash].js',
        assetFileNames: 'assets/[name]-[hash].[ext]',
      },
    },
    chunkSizeWarningLimit: 1000,
  },
  server: {
    port: 5173,
    host: true,
    proxy: {
      '/api': {
        target: process.env.VITE_API_URL || 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    },
  },
  preview: {
    port: 4173,
    host: true,
  },
  envPrefix: 'VITE_',
});