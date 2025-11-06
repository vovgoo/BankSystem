import { useMutation, useQueryClient } from '@tanstack/react-query';
import { clientsService } from '@api';
import type { CreateClientRequest, UpdateClientRequest, UUID } from '@api';

export const useCreateClient = (onSuccess?: () => void): ReturnType<typeof useMutation> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateClientRequest) => clientsService.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clients'] });
      onSuccess?.();
    },
  });
};

export const useUpdateClient = (onSuccess?: () => void): ReturnType<typeof useMutation> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: UpdateClientRequest) => clientsService.update(data),
    onSuccess: (unusedResponse, variables) => {
      queryClient.invalidateQueries({ queryKey: ['client', variables.id] });
      queryClient.invalidateQueries({ queryKey: ['clients'] });
      onSuccess?.();
    },
  });
};

export const useDeleteClient = (onSuccess?: () => void): ReturnType<typeof useMutation> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (clientId: UUID) => clientsService.delete(clientId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clients'] });
      onSuccess?.();
    },
  });
};
