import { z } from 'zod';

export const transferSchema = z.object({
  fromAccountId: z.string().uuid('ID счета отправителя обязателен'),
  toAccountId: z.string().uuid('ID счета получателя обязателен'),
  amount: z
    .number()
    .min(0.01, 'Сумма перевода должна быть больше 0')
    .refine(
      (val) => /^\d+(\.\d{1,2})?$/.test(val.toString()),
      'Сумма должна иметь не более 2 цифр после запятой'
    ),
});

export type TransferFormData = z.infer<typeof transferSchema>;
