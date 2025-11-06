import { useCallback, memo } from 'react';
import { Box, Table, ButtonGroup, IconButton, Pagination } from '@chakra-ui/react';
import { LuChevronLeft, LuChevronRight } from 'react-icons/lu';
import { TABLE_MIN_WIDTH } from '@/constants';
import { colors } from '@/theme';

export interface Column<T> {
  title: string;
  key: keyof T | string;
  width?: string | number;
  align?: 'left' | 'center' | 'right';
  render?: (value: unknown, row: T) => React.ReactNode;
}

interface BaseTableProps<T extends object> {
  columns: Column<T>[];
  data: T[];
  totalElements: number;
  pageParams: { page: number; size: number };
  setPageParams: (params: { page: number; size: number }) => void;
}

const ResponsiveTableComponent = <T extends object>({
  columns,
  data,
  totalElements,
  pageParams,
  setPageParams,
}: BaseTableProps<T>) => {
  const handlePageChange = useCallback(
    (page: { page: number }) => {
      setPageParams({ ...pageParams, page: page.page - 1 });
    },
    [pageParams, setPageParams]
  );

  return (
    <Box>
      {totalElements > 0 && (
        <Box mb={4}>
          <Pagination.Root
            count={totalElements}
            pageSize={pageParams.size}
            page={pageParams.page + 1}
            onPageChange={handlePageChange}
          >
            <ButtonGroup variant="ghost" size="sm" wrap="wrap">
              <Pagination.PrevTrigger asChild>
                <IconButton aria-label="Previous page">
                  <LuChevronLeft />
                </IconButton>
              </Pagination.PrevTrigger>

              <Pagination.Items
                render={(page) => (
                  <IconButton
                    key={page.value}
                    aria-label={`Page ${page.value}`}
                    variant={{ base: 'ghost', _selected: 'outline' }}
                  >
                    {page.value}
                  </IconButton>
                )}
              />

              <Pagination.NextTrigger asChild>
                <IconButton aria-label="Next page">
                  <LuChevronRight />
                </IconButton>
              </Pagination.NextTrigger>
            </ButtonGroup>
          </Pagination.Root>
        </Box>
      )}
      <Box overflowX="auto" rounded="5px">
        <Table.Root size="sm" showColumnBorder minWidth={`${TABLE_MIN_WIDTH}px`}>
          <Table.Header>
            <Table.Row bg={colors.background.table}>
              {columns.map((col) => (
                <Table.ColumnHeader
                  key={String(col.key)}
                  textAlign={col.align || 'left'}
                  width={col.width}
                >
                  {col.title}
                </Table.ColumnHeader>
              ))}
            </Table.Row>
          </Table.Header>

          <Table.Body>
            {data.map((row, idx) => {
              const rowKey =
                'id' in row && typeof (row as { id?: string }).id === 'string'
                  ? (row as { id: string }).id
                  : idx;
              return (
                <Table.Row key={rowKey} bg={colors.background.table}>
                  {columns.map((col) => {
                    const key = String(col.key);
                    const value = key in row ? (row as Record<string, unknown>)[key] : undefined;
                    return (
                      <Table.Cell key={key} textAlign={col.align || 'left'}>
                        {col.render ? col.render(value, row) : String(value ?? '')}
                      </Table.Cell>
                    );
                  })}
                </Table.Row>
              );
            })}
          </Table.Body>
        </Table.Root>
      </Box>
    </Box>
  );
};

export const ResponsiveTable = memo(ResponsiveTableComponent) as typeof ResponsiveTableComponent;
