import { useState } from 'react';
import type { PageParams } from '@api';

export function usePagination(initialPage = 0, initialSize = 10) {
  const [params, setParams] = useState<PageParams>({
    page: initialPage,
    size: initialSize,
  });
  return { ...params, setParams };
}
