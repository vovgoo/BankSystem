import { useMutation, useQueryClient, type UseMutationResult } from '@tanstack/react-query';
import { accountsService } from '@api';
import type { UUID, TransactionResponse } from '@api';
import type { DepositFormData, WithdrawFormData, TransferFormData } from '@schemas';

export const useCreateAccount = (
  clientId: UUID,
  onSuccess?: () => void
): UseMutationResult<TransactionResponse, Error, void> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => accountsService.create({ clientId }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['client', clientId] });
      onSuccess?.();
    },
  });
};

export const useDepositAccount = (
  onSuccess?: () => void
): UseMutationResult<TransactionResponse, Error, DepositFormData> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: DepositFormData) => accountsService.deposit(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['client'] });
      onSuccess?.();
    },
  });
};

export const useWithdrawAccount = (
  onSuccess?: () => void
): UseMutationResult<TransactionResponse, Error, WithdrawFormData> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: WithdrawFormData) => accountsService.withdraw(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['client'] });
      onSuccess?.();
    },
  });
};

export const useTransferAccount = (
  onSuccess?: () => void
): UseMutationResult<TransactionResponse, Error, TransferFormData> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: TransferFormData) => accountsService.transfer(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['client'] });
      onSuccess?.();
    },
  });
};

export const useDeleteAccount = (
  onSuccess?: () => void
): UseMutationResult<TransactionResponse, Error, UUID> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (accountId: UUID) => accountsService.delete(accountId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['client'] });
      onSuccess?.();
    },
  });
};
