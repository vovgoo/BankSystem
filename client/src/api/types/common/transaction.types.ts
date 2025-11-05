import type { DateTime } from './base.types';

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
