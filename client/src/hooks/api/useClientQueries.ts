import { useQuery, type UseQueryOptions } from '@tanstack/react-query';
import { clientsService } from '@api';
import type { PageParams, SearchClientRequest, GetClientResponse } from '@api';

export const useClient = (
  id: string,
  pageParams: PageParams,
  options?: Omit<UseQueryOptions<GetClientResponse>, 'queryKey' | 'queryFn'>
): ReturnType<typeof useQuery<GetClientResponse>> => {
  return useQuery({
    queryKey: ['client', id, pageParams.page, pageParams.size],
    queryFn: () => clientsService.getById(id, pageParams),
    enabled: !!id,
    ...options,
  });
};

export const useClients = (
  searchRequest: SearchClientRequest,
  pageParams: PageParams
): ReturnType<typeof useQuery> => {
  return useQuery({
    queryKey: ['clients', searchRequest.lastName, pageParams.page, pageParams.size],
    queryFn: () => clientsService.search(searchRequest, pageParams),
  });
};
