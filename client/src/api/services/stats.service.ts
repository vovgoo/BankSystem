import { apiClient } from '../client';
import type { StatsResponse } from '../types/stats.types';

export class StatsService {
  private readonly basePath = '/api/v1/stats';

  async getOverview(): Promise<StatsResponse> {
    return apiClient.get<StatsResponse>(this.basePath);
  }
}

export const statsService = new StatsService();
