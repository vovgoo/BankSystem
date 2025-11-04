import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Dashboard, Clients, ClientDetails, NotFound } from '@pages';
import { Header } from '@components';
import { AppRoutes } from './routes';
import { Box } from '@chakra-ui/react';

function App() {
  return (
    <Box minH="100vh" display="flex" flexDirection="column" bg="#040404">
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path={AppRoutes.DASHBOARD} element={<Dashboard />} />
          <Route path={AppRoutes.CLIENTS} element={<Clients />} />
          <Route path={AppRoutes.CLIENT_DETAILS} element={<ClientDetails />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </Box>
  );
}

export default App;
