import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { Provider, Toaster } from '@components';
import { toaster } from '@components';
import { initializeWithToaster } from '@api';
import type { Toaster as ApiToaster } from '@api';
import './index.css';
import App from './App';

initializeWithToaster(() => toaster as unknown as ApiToaster);

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Provider>
      <Toaster />
      <App />
    </Provider>
  </StrictMode>
);
