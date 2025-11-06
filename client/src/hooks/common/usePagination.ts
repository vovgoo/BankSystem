import { useState } from 'react';
import type { PageParams } from '@api';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from '@/constants';

export function usePagination(
  initialPage = DEFAULT_PAGE,
  initialSize = DEFAULT_PAGE_SIZE
): PageParams & { setParams: (params: PageParams) => void } {
  const [params, setParams] = useState<PageParams>({
    page: initialPage,
    size: initialSize,
  });
  return { ...params, setParams };
}
