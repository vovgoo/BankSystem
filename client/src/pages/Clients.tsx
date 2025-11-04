import { useEffect, useState } from 'react';
import { Box } from '@chakra-ui/react';
import { clientsService } from '@api';
import type { ClientListItem, PageParams, PageResponse } from '@api';
import { ClientsHeader, PageLoader, ClientsTable, LoadingError, EmptyState } from '@components';
import { FiUsers } from 'react-icons/fi';

export const Clients: React.FC = () => {
  const [data, setData] = useState<PageResponse<ClientListItem> | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<boolean>(false);
  const [search, setSearch] = useState<string>('');
  const [pageParams, setPageParams] = useState<PageParams>({ page: 0, size: 10 });

  const fetchData = async (searchValue: string = search) => {
    setIsLoading(true);
    setError(false);
    try {
      const res = await clientsService.search({ lastName: searchValue }, pageParams);
      setData(res);
    } catch (err) {
      console.error(err);
      setError(true);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    const handler = setTimeout(() => {
      fetchData(search);
    }, 500);
    return () => clearTimeout(handler);
  }, [search, pageParams]);

  return (
    <Box flex={1} display="flex" flexDirection="column" p={6} overflow="auto">
      <ClientsHeader
        search={search}
        onSearchChange={(val) => {
          setSearch(val);
          setPageParams((prev) => ({ ...prev, page: 0 }));
        }}
        onCreateSuccess={() => fetchData(search)}
      />

      {error ? (
        <LoadingError onRetry={() => fetchData(search)} />
      ) : isLoading || !data ? (
        <PageLoader title="Загружаем пользователей" />
      ) : data?.content && data.content.length > 0 ? (
        <ClientsTable
          data={data}
          pageParams={pageParams}
          setPageParams={setPageParams}
          onActionSuccess={() => fetchData(search)}
        />
      ) : (
        <EmptyState icon={<FiUsers size="30px" />} title={'Клиенты не найдены'} />
      )}
    </Box>
  );
};
