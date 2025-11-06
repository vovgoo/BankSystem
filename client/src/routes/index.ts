export const AppRoutes = {
  DASHBOARD: '/',
  CLIENTS: '/clients',
  CLIENT_DETAILS: '/client/:id',
} as const;

export const createClientDetailsRoute = (id: string): string =>
  AppRoutes.CLIENT_DETAILS.replace(':id', id);
