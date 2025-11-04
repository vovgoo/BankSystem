export interface StatsResponse {
  totalClients: number;
  clientsWithAccounts: number;
  clientsWithoutAccounts: number;
  totalAccounts: number;
  totalBalance: number;
  averageBalance: number;
  maxBalance: number;
  minBalance: number;
}
