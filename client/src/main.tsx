import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Provider, Toaster, toaster } from '@components';
import { initializeWithToaster } from '@api';
import type { Toaster as ApiToaster } from '@api';
import './index.css';
import App from './App';

const queryClient = new QueryClient();
initializeWithToaster(() => toaster as unknown as ApiToaster);

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <Provider>
        <Toaster />
        <App />
      </Provider>
    </QueryClientProvider>
  </StrictMode>
);
