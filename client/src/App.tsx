import { lazy, Suspense } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { MainLayout, ErrorBoundary, LoadingView } from '@components';
import { AppRoutes } from './routes';

const Dashboard = lazy(() => import('@pages').then((m) => ({ default: m.Dashboard })));
const Clients = lazy(() => import('@pages').then((m) => ({ default: m.Clients })));
const ClientDetails = lazy(() => import('@pages').then((m) => ({ default: m.ClientDetails })));
const NotFound = lazy(() => import('@pages').then((m) => ({ default: m.NotFound })));

function App() {
  return (
    <BrowserRouter>
      <ErrorBoundary>
        <MainLayout>
          <Suspense fallback={<LoadingView title="Загрузка страницы..." />}>
            <Routes>
              <Route path={AppRoutes.DASHBOARD} element={<Dashboard />} />
              <Route path={AppRoutes.CLIENTS} element={<Clients />} />
              <Route path={AppRoutes.CLIENT_DETAILS} element={<ClientDetails />} />
              <Route path="*" element={<NotFound />} />
            </Routes>
          </Suspense>
        </MainLayout>
      </ErrorBoundary>
    </BrowserRouter>
  );
}

export default App;
