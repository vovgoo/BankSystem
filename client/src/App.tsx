import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Dashboard, Users, UserDetails } from '@pages';
import { Header } from '@components';
import { AppRoutes } from './routes';

function App() {
  return (
    <div className='bg-[#040404] min-h-dvh w-full' >
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path={AppRoutes.DASHBOARD} element={<Dashboard />} />
          <Route path={AppRoutes.USERS} element={<Users />} />
          <Route path={AppRoutes.USER_DETAIL} element={<UserDetails />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;