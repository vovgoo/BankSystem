import { z } from 'zod';
import { createClientSchema } from './create-client.schema';

export const updateClientSchema = createClientSchema.extend({
  id: z.string().uuid('ID клиента должен быть UUID'),
});

export type UpdateClientFormData = z.infer<typeof updateClientSchema>;
