import { useMemo, memo } from 'react';
import { ResponsiveTable, CopyableId } from '@components';
import type { Column } from '@components';
import type { ClientListItem, PageParams, PageResponse } from '@api';
import { ClientActionMenu } from '@components';
import { formatPhone, formatCurrency } from '@utils';

interface ClientsTableProps {
  data: PageResponse<ClientListItem>;
  pageParams: PageParams;
  setPageParams: (params: PageParams) => void;
  onActionSuccess: () => void;
}

export const ClientsTable: React.FC<ClientsTableProps> = memo(
  ({ data, pageParams, setPageParams, onActionSuccess }) => {
    const columns: Column<ClientListItem>[] = useMemo(
      () => [
        {
          title: 'Идентификатор',
          key: 'id',
          align: 'left',
          render: (value: unknown) => (
            <CopyableId id={value as string} label="Идентификатор клиента" />
          ),
        },
        { title: 'Фамилия', key: 'lastName', align: 'left' },
        {
          title: 'Телефон',
          key: 'phone',
          align: 'center',
          render: (value: unknown) => formatPhone(value as string),
        },
        {
          title: 'Общий баланс',
          key: 'totalBalance',
          align: 'right',
          render: (value: unknown) => formatCurrency(value as number),
        },
        {
          title: 'Действия',
          key: 'actions',
          align: 'center',
          render: (_: unknown, row: ClientListItem) => (
            <ClientActionMenu clientId={row.id} onSuccess={onActionSuccess} />
          ),
        },
      ],
      [onActionSuccess]
    );

    return (
      <ResponsiveTable
        columns={columns}
        data={data.content}
        totalElements={data.totalElements}
        pageParams={pageParams}
        setPageParams={setPageParams}
      />
    );
  },
  (prevProps, nextProps) => {
    return (
      prevProps.data === nextProps.data &&
      prevProps.pageParams.page === nextProps.pageParams.page &&
      prevProps.pageParams.size === nextProps.pageParams.size &&
      prevProps.onActionSuccess === nextProps.onActionSuccess
    );
  }
);
