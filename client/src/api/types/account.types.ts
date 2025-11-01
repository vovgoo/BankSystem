import type { UUID, TransactionResponse } from './index';

export interface CreateAccountRequest {
  clientId: UUID;
}

export interface DepositAccountRequest {
  accountId: UUID;
  amount: number;
}

export interface WithdrawAccountRequest {
  accountId: UUID;
  amount: number;
}

export interface TransferAccountRequest {
  fromAccountId: UUID;
  toAccountId: UUID;
  amount: number;
}

export type CreateAccountResponse = TransactionResponse;
export type DepositAccountResponse = TransactionResponse;
export type WithdrawAccountResponse = TransactionResponse;
export type TransferAccountResponse = TransactionResponse;
export type DeleteAccountResponse = TransactionResponse;