import { useState, useCallback, useMemo } from 'react';
import { Box } from '@chakra-ui/react';
import { ClientsHeader, LoadingView, ClientsTable, ErrorView, EmptyView } from '@components';
import { FiUsers } from 'react-icons/fi';
import { useDebounce, usePagination, useClients } from '@/hooks';
import { DEBOUNCE_DELAY, DEFAULT_PAGE } from '@/constants';

export const Clients: React.FC = () => {
  const [search, setSearch] = useState('');
  const debouncedSearch = useDebounce(search, DEBOUNCE_DELAY);
  const { page, size, setParams } = usePagination();

  const { data, isLoading, error, refetch } = useClients(
    { lastName: debouncedSearch },
    { page, size }
  );

  const handleSearchChange = useCallback(
    (val: string) => {
      setSearch(val);
      setParams({ page: DEFAULT_PAGE, size });
    },
    [setParams, size]
  );

  const pageParams = useMemo(() => ({ page, size }), [page, size]);
  const handleRetry = useCallback(() => {
    refetch();
  }, [refetch]);
  const handleActionSuccess = useCallback(() => {
    refetch();
  }, [refetch]);

  return (
    <Box flex={1} display="flex" flexDirection="column" p={6} overflow="auto">
      <ClientsHeader
        search={search}
        onSearchChange={handleSearchChange}
        onCreateSuccess={handleActionSuccess}
      />
      {error ? (
        <ErrorView onRetry={handleRetry} />
      ) : isLoading || !data ? (
        <LoadingView title="Загружаем пользователей" />
      ) : data.content.length > 0 ? (
        <ClientsTable
          data={data}
          pageParams={pageParams}
          setPageParams={setParams}
          onActionSuccess={handleActionSuccess}
        />
      ) : (
        <EmptyView icon={<FiUsers size="30px" />} title="Клиенты не найдены" />
      )}
    </Box>
  );
};
