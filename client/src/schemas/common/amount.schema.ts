import { z } from 'zod';

export const amountSchema = z
  .number()
  .min(0.01, 'Сумма должна быть больше 0')
  .refine(
    (val) => /^\d+(\.\d{1,2})?$/.test(val.toString()),
    'Сумма должна иметь не более 2 цифр после запятой'
  );
