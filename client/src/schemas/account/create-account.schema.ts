import { z } from 'zod';

export const createAccountSchema = z.object({
  clientId: z.string().uuid('ID клиента обязателен'),
});

export type CreateAccountFormData = z.infer<typeof createAccountSchema>;
