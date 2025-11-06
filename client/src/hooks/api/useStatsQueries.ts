import { useQuery } from '@tanstack/react-query';
import { statsService } from '@api';

export const useStatsOverview = (): ReturnType<typeof useQuery> => {
  return useQuery({
    queryKey: ['stats', 'overview'],
    queryFn: () => statsService.getOverview(),
  });
};
