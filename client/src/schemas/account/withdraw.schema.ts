import { z } from 'zod';

export const withdrawSchema = z.object({
  accountId: z.string().uuid('ID счета обязателен'),
  amount: z
    .number()
    .min(0.01, 'Сумма снятия должна быть больше 0')
    .refine(
      (val) => /^\d+(\.\d{1,2})?$/.test(val.toString()),
      'Сумма должна иметь не более 2 цифр после запятой'
    ),
});

export type WithdrawFormData = z.infer<typeof withdrawSchema>;
