import { z } from 'zod';

export const depositSchema = z.object({
  accountId: z.string().uuid('ID счета обязателен'),
  amount: z
    .number()
    .min(0.01, 'Сумма пополнения должна быть больше 0')
    .refine(
      (val) => /^\d+(\.\d{1,2})?$/.test(val.toString()),
      'Сумма должна иметь не более 2 цифр после запятой'
    ),
});

export type DepositFormData = z.infer<typeof depositSchema>;
