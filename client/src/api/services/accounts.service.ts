import { apiClient } from '../client';
import type {
  CreateAccountRequest,
  CreateAccountResponse,
  DepositAccountRequest,
  DepositAccountResponse,
  WithdrawAccountRequest,
  WithdrawAccountResponse,
  TransferAccountRequest,
  TransferAccountResponse,
  DeleteAccountResponse,
} from '../types/account.types';

export class AccountsService {
  private readonly basePath = '/api/v1/accounts';

  async create(data: CreateAccountRequest): Promise<CreateAccountResponse> {
    return apiClient.post<CreateAccountResponse>(this.basePath, data);
  }

  async deposit(data: DepositAccountRequest): Promise<DepositAccountResponse> {
    return apiClient.post<DepositAccountResponse>(`${this.basePath}/deposit`, data);
  }

  async withdraw(data: WithdrawAccountRequest): Promise<WithdrawAccountResponse> {
    return apiClient.post<WithdrawAccountResponse>(`${this.basePath}/withdraw`, data);
  }

  async transfer(data: TransferAccountRequest): Promise<TransferAccountResponse> {
    return apiClient.post<TransferAccountResponse>(`${this.basePath}/transfer`, data);
  }

  async delete(id: string): Promise<DeleteAccountResponse> {
    return apiClient.delete<DeleteAccountResponse>(`${this.basePath}/${id}`);
  }
}

export const accountsService = new AccountsService();
