import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Provider, Toaster, toaster } from '@components';
import { initializeWithToaster } from '@api';
import type { Toaster as ApiToaster } from '@api';
import { QUERY_STALE_TIME, QUERY_GC_TIME } from '@/constants';
import './styles/index.css';
import App from './App';

initializeWithToaster(() => toaster as unknown as ApiToaster);

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: (failureCount: number, error: unknown): boolean => {
        if (error && typeof error === 'object' && 'status' in error) {
          const status = error.status as number;
          if (status >= 400 && status < 500) {
            return false;
          }
        }
        return failureCount < 1;
      },
      staleTime: QUERY_STALE_TIME,
      gcTime: QUERY_GC_TIME,
    },
    mutations: {
      retry: false,
    },
  },
});

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
