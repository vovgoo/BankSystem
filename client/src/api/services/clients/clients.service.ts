import { apiClient } from '../../client';
import type {
  CreateClientRequest,
  CreateClientResponse,
  UpdateClientRequest,
  UpdateClientResponse,
  DeleteClientResponse,
  GetClientResponse,
  SearchClientsResponse,
  SearchClientRequest,
} from '../../types/client';
import type { PageParams } from '../../types/common';

export class ClientsService {
  private readonly basePath = '/api/v1/clients';

  async create(data: CreateClientRequest): Promise<CreateClientResponse> {
    return apiClient.post<CreateClientResponse>(this.basePath, data);
  }

  async update(data: UpdateClientRequest): Promise<UpdateClientResponse> {
    return apiClient.put<UpdateClientResponse>(this.basePath, data);
  }

  async getById(id: string, pageParams: PageParams): Promise<GetClientResponse> {
    const params = new URLSearchParams({
      page: pageParams.page.toString(),
      size: pageParams.size.toString(),
    });

    return apiClient.get<GetClientResponse>(`${this.basePath}/${id}?${params.toString()}`);
  }

  async delete(id: string): Promise<DeleteClientResponse> {
    return apiClient.delete<DeleteClientResponse>(`${this.basePath}/${id}`);
  }

  async search(
    searchRequest: SearchClientRequest,
    pageParams: PageParams
  ): Promise<SearchClientsResponse> {
    const params = new URLSearchParams({
      lastName: searchRequest.lastName,
      page: pageParams.page.toString(),
      size: pageParams.size.toString(),
    });

    return apiClient.get<SearchClientsResponse>(`${this.basePath}/search?${params.toString()}`);
  }
}

export const clientsService = new ClientsService();
