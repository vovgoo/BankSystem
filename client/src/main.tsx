import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { Provider, Toaster } from '@components';
import './index.css';
import App from './App';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Provider>
      <Toaster />
      <App />
    </Provider>
  </StrictMode>
);
