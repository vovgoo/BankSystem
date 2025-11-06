import { useMemo, useCallback } from 'react';
import { Box } from '@chakra-ui/react';
import { useParams, Navigate } from 'react-router-dom';
import {
  ClientDetailsHeader,
  AccountsHeader,
  AccountsTable,
  EmptyView,
  ErrorView,
  LoadingView,
} from '@components';
import { FiCreditCard } from 'react-icons/fi';
import { usePagination, useClient } from '@/hooks';
import { validateUuid } from '@utils';
import { AppRoutes } from '@routes';
import { NotFound } from './NotFound';

export const ClientDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const { page, size, setParams } = usePagination();

  const validId = useMemo(() => validateUuid(id), [id]);
  const pageParams = useMemo(() => ({ page, size }), [page, size]);

  const {
    data: client,
    isLoading,
    error,
    refetch,
  } = useClient(
    validId || '',
    { page, size },
    {
      enabled: !!validId,
    }
  );

  const handleRetry = useCallback(() => {
    refetch();
  }, [refetch]);

  const handleActionSuccess = useCallback(() => {
    refetch();
  }, [refetch]);

  if (!validId) {
    return <Navigate to={AppRoutes.CLIENTS} replace />;
  }

  if (error && !isLoading) {
    return <NotFound />;
  }

  return (
    <Box flex={1} display="flex" flexDirection="column" p={6} overflow="auto">
      <ClientDetailsHeader
        client={client ?? null}
        isLoading={isLoading}
        onActionSuccess={handleActionSuccess}
      />
      {error ? (
        <ErrorView onRetry={handleRetry} />
      ) : isLoading || !client ? (
        <LoadingView title="Загружаем счета клиента" />
      ) : (
        <>
          <AccountsHeader
            client={client}
            isLoading={isLoading}
            onActionSuccess={handleActionSuccess}
          />
          {client.accounts.content.length > 0 ? (
            <AccountsTable
              data={client.accounts}
              pageParams={pageParams}
              setPageParams={setParams}
              onActionSuccess={handleActionSuccess}
            />
          ) : (
            <EmptyView icon={<FiCreditCard size="30px" />} title="Счета не найдены" />
          )}
        </>
      )}
    </Box>
  );
};
