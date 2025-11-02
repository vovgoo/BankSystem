import { z } from 'zod';

export const createClientSchema = z.object({
  lastName: z
    .string()
    .min(2, "Фамилия должна быть от 2 символов")
    .max(50, "Фамилия должна быть не больше 50 символов")
    .regex(/^[A-Za-zА-Яа-яЁё]+(-[A-Za-zА-Яа-яЁё]+)?$/, "Фамилия должна быть одним словом или двойная через тире"),
  phone: z
    .string()
    .regex(/^\+375\d{9}$/, "Телефон должен быть в формате +375XXXXXXXXX"),
});

export const updateClientSchema = createClientSchema.extend({
  id: z.string().uuid("ID клиента должен быть UUID"),
});

export const searchClientSchema = z.object({
  lastName: z.string().optional(), 
});