import { z } from 'zod';
import { amountSchema } from '../common';

export const transferSchema = z.object({
  fromAccountId: z.string().uuid('ID счета отправителя обязателен'),
  toAccountId: z.string().uuid('ID счета получателя обязателен'),
  amount: amountSchema,
});

export type TransferFormData = z.infer<typeof transferSchema>;
