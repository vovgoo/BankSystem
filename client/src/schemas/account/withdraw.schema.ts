import { z } from 'zod';
import { amountSchema } from '../common';

export const withdrawSchema = z.object({
  accountId: z.string().uuid('ID счета обязателен'),
  amount: amountSchema,
});

export type WithdrawFormData = z.infer<typeof withdrawSchema>;
