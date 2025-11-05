import type { UUID } from '../common';
import type { PageResponse } from '../common';
import type { TransactionResponse } from '../common';
import type { AccountSummaryResponse } from '../account';

export interface CreateClientRequest {
  lastName: string;
  phone: string;
}

export interface UpdateClientRequest {
  id: UUID;
  lastName: string;
  phone: string;
}

export interface SearchClientRequest {
  lastName: string;
}

export interface ClientListItem {
  id: UUID;
  lastName: string;
  phone: string;
  totalBalance: number;
}

export interface ClientDetailsResponse {
  id: UUID;
  lastName: string;
  phone: string;
  accounts: PageResponse<AccountSummaryResponse>;
}

export type CreateClientResponse = TransactionResponse;
export type UpdateClientResponse = TransactionResponse;
export type DeleteClientResponse = TransactionResponse;
export type GetClientResponse = ClientDetailsResponse;
export type SearchClientsResponse = PageResponse<ClientListItem>;
