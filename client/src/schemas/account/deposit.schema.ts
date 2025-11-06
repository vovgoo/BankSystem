import { z } from 'zod';
import { amountSchema } from '../common';

export const depositSchema = z.object({
  accountId: z.string().uuid('ID счета обязателен'),
  amount: amountSchema,
});

export type DepositFormData = z.infer<typeof depositSchema>;
