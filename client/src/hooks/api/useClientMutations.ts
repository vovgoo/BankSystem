import { useMutation, useQueryClient, type UseMutationResult } from '@tanstack/react-query';
import { clientsService } from '@api';
import type { CreateClientRequest, UpdateClientRequest, UUID, TransactionResponse } from '@api';

export const useCreateClient = (
  onSuccess?: () => void
): UseMutationResult<TransactionResponse, Error, CreateClientRequest> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateClientRequest) => clientsService.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clients'] });
      onSuccess?.();
    },
  });
};

export const useUpdateClient = (
  onSuccess?: () => void
): UseMutationResult<TransactionResponse, Error, UpdateClientRequest> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: UpdateClientRequest) => clientsService.update(data),
    onSuccess: (_: TransactionResponse, variables) => {
      queryClient.invalidateQueries({ queryKey: ['client', variables.id] });
      queryClient.invalidateQueries({ queryKey: ['clients'] });
      onSuccess?.();
    },
  });
};

export const useDeleteClient = (
  onSuccess?: () => void
): UseMutationResult<TransactionResponse, Error, UUID> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (clientId: UUID) => clientsService.delete(clientId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clients'] });
      onSuccess?.();
    },
  });
};
