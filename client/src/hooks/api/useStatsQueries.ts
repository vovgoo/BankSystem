import { useQuery } from '@tanstack/react-query';
import { statsService } from '@api';

export const useStatsOverview = () => {
  return useQuery({
    queryKey: ['stats', 'overview'],
    queryFn: () => statsService.getOverview(),
  });
};
