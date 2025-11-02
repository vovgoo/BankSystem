import { z } from 'zod';

export const pageParamsSchema = z.object({
  page: z.number().min(0, "Номер страницы не может быть отрицательным"),
  size: z.number().min(1, "Размер страницы не может быть меньше 1").max(100, "Размер страницы не может быть больше 100"),
});

export type PageParams = z.infer<typeof pageParamsSchema>;