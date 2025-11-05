import type { AxiosError, InternalAxiosRequestConfig } from 'axios';
import type { TransactionResponse } from '../types';
import { formatDate } from '@utils';
import { Box, Text } from '@chakra-ui/react';

export interface Toaster {
  success: (options: { title: string; description?: unknown }) => void;
  error: (options: { title: string; description?: string | React.ReactNode }) => void;
}

export function isTransactionRequest(config: InternalAxiosRequestConfig): boolean {
  const method = config.method?.toUpperCase();
  const url = config.url || '';

  const isMutation =
    method === 'POST' || method === 'PUT' || method === 'PATCH' || method === 'DELETE';

  const transactionEndpoints = [
    '/api/v1/clients',
    '/api/v1/accounts',
    '/deposit',
    '/withdraw',
    '/transfer',
  ];

  const isTransactionEndpoint = transactionEndpoints.some((endpoint) => url.includes(endpoint));

  const isGetRequest = method === 'GET';

  return isMutation && isTransactionEndpoint && !isGetRequest;
}

export function showTransactionSuccess(toaster: Toaster): void {
  toaster.success({ title: 'Транзакция прошла успешно' });
}

export function showTransactionError(
  error: AxiosError<TransactionResponse>,
  toaster: Toaster
): void {
  const errorResponse = error.response?.data;

  if (errorResponse) {
    toaster.error({
      title: 'Транзакция не была проведена',
      description: (
        <>
          <Text>{errorResponse.message}</Text>
          <Box textAlign="right" fontSize="sm">
            {formatDate(errorResponse.timestamp)}
          </Box>
        </>
      ),
    });
  } else {
    toaster.error({
      title: 'Транзакция не была проведена',
      description: 'Ошибка сервиса. Попробуйте позже',
    });
  }
}
