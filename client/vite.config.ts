import path from 'path';
import { fileURLToPath } from 'url';

import react from '@vitejs/plugin-react-swc';
import { defineConfig } from 'vite';
import tailwindcss from '@tailwindcss/vite';

const dirname = path.dirname(fileURLToPath(import.meta.url));
const srcPath = path.resolve(dirname, 'src');

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
    dedupe: ['react', 'react-dom', '@tanstack/react-query', '@emotion/react', '@emotion/styled'],
  },
  optimizeDeps: {
    include: [
      'react',
      'react-dom',
      'react/jsx-runtime',
      'react/jsx-dev-runtime',
      'scheduler',
      '@tanstack/react-query',
      '@emotion/react',
      '@emotion/styled',
      '@chakra-ui/react',
    ],
    esbuildOptions: {
      target: 'esnext',
    },
    force: false,
  },
  build: {
    outDir: 'dist',
    sourcemap: true,
    commonjsOptions: {
      include: [/node_modules/],
      transformMixedEsModules: true,
    },
    rollupOptions: {
      preserveEntrySignatures: 'strict',
      output: {
        manualChunks: undefined,
        entryFileNames: 'js/[name]-[hash].js',
        chunkFileNames: 'js/[name]-[hash].js',
        assetFileNames: 'assets/[name]-[hash].[ext]',
      },
    },
    chunkSizeWarningLimit: 1000,
    modulePreload: {
      polyfill: true,
    },
    minify: 'esbuild',
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