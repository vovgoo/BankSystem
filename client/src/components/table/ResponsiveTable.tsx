import React from 'react';
import { Box, Table, ButtonGroup, IconButton, Pagination } from '@chakra-ui/react';
import { LuChevronLeft, LuChevronRight } from 'react-icons/lu';

export interface Column<T> {
  title: string;
  key: keyof T | string;
  width?: string | number;
  align?: 'left' | 'center' | 'right';
  render?: (value: any, row: T) => React.ReactNode;
}

interface ResponsiveTableProps<T> {
  columns: Column<T>[];
  data: T[];
  totalElements: number;
  pageParams: { page: number; size: number };
  setPageParams: (params: { page: number; size: number }) => void;
}

export const ResponsiveTable: React.FC<ResponsiveTableProps<any>> = ({
  columns,
  data,
  totalElements,
  pageParams,
  setPageParams,
}) => {
  return (
    <Box>
      {totalElements > 0 && (
        <Box mb={4}>
          <Pagination.Root
            count={totalElements}
            pageSize={pageParams.size}
            page={pageParams.page + 1}
            onPageChange={(page) =>
              setPageParams({ ...pageParams, page: page.page - 1 })
            }
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
        <Table.Root size="sm" variant="outline" striped showColumnBorder minWidth="600px">
          <Table.Header>
            <Table.Row>
              {columns.map(col => (
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
            {data.map((row, idx) => (
              <Table.Row key={idx}>
                {columns.map(col => (
                  <Table.Cell key={String(col.key)} textAlign={col.align || 'left'}>
                    {col.render ? col.render(row[col.key], row) : row[col.key]}
                  </Table.Cell>
                ))}
              </Table.Row>
            ))}
          </Table.Body>
        </Table.Root>
      </Box>
    </Box>
  );
};