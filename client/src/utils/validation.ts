import { z } from 'zod';
import { MAX_PAGE_SIZE } from '@/constants';

export const uuidSchema = z.string().uuid('Некорректный формат идентификатора');

export const validateUuid = (id: string | undefined): string | null => {
  if (!id) return null;
  try {
    uuidSchema.parse(id);
    return id;
  } catch {
    return null;
  }
};

export const validatePage = (page: number): number => {
  return Math.max(0, Math.floor(page));
};

export const validatePageSize = (size: number, maxSize = MAX_PAGE_SIZE): number => {
  return Math.max(1, Math.min(maxSize, Math.floor(size)));
};
