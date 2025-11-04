export type UUID = string;

export type DateTime = string;

export type TransactionStatus = 'APPROVED' | 'DECLINED';

export const TransactionStatus = {
  APPROVED: 'APPROVED' as const,
  DECLINED: 'DECLINED' as const,
} as const;

export interface TransactionResponse {
  status: TransactionStatus;
  message: string;
  timestamp: DateTime;
}

export type {
  CreateClientRequest,
  UpdateClientRequest,
  SearchClientRequest,
  ClientListItem,
  ClientDetailsResponse,
  AccountSummaryResponse,
  CreateClientResponse,
  UpdateClientResponse,
  DeleteClientResponse,
  GetClientResponse,
  SearchClientsResponse,
} from './client.types';
export type {
  CreateAccountRequest,
  DepositAccountRequest,
  WithdrawAccountRequest,
  TransferAccountRequest,
  CreateAccountResponse,
  DepositAccountResponse,
  WithdrawAccountResponse,
  TransferAccountResponse,
  DeleteAccountResponse,
} from './account.types';
export type { StatsResponse } from './stats.types';
export type { PageParams, PageResponse } from './pagination.types';
