import { z } from 'zod';

export const createAccountSchema = z.object({
  clientId: z.string().uuid('ID клиента обязателен'),
});

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

export type CreateAccountFormData = z.infer<typeof createAccountSchema>;
export type DepositFormData = z.infer<typeof depositSchema>;
export type WithdrawFormData = z.infer<typeof withdrawSchema>;
export type TransferFormData = z.infer<typeof transferSchema>;
