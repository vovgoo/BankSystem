import { useMemo, memo } from 'react';
import { ResponsiveTable, CopyableId } from '@components';
import type { Column } from '@components';
import type { AccountSummaryResponse, PageParams, PageResponse } from '@api';
import { AccountActionMenu } from '@components';
import { formatCurrency } from '@utils';

interface AccountsTableProps {
  data: PageResponse<AccountSummaryResponse>;
  pageParams: PageParams;
  setPageParams: (params: PageParams) => void;
  onActionSuccess: () => void;
}

export const AccountsTable: React.FC<AccountsTableProps> = memo(
  ({ data, pageParams, setPageParams, onActionSuccess }) => {
    const columns: Column<AccountSummaryResponse>[] = useMemo(
      () => [
        {
          title: 'Идентификатор',
          key: 'id',
          align: 'left',
          render: (value: unknown) => (
            <CopyableId id={value as string} label="Идентификатор счёта" />
          ),
        },
        {
          title: 'Баланс',
          key: 'balance',
          align: 'right',
          render: (value: unknown) => formatCurrency(value as number),
        },
        {
          title: 'Действия',
          key: 'actions',
          align: 'center',
          render: (unusedValue: unknown, row: AccountSummaryResponse) => (
            <AccountActionMenu accountId={row.id} onSuccess={onActionSuccess} />
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
