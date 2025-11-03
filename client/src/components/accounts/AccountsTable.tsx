import React from "react";
import { ResponsiveTable, Column, CopyableId } from "@components";
import type { AccountSummaryResponse, PageParams, PageResponse } from "@api";
import { AccountActionMenu } from "@components";

interface AccountsTableProps {
  data: PageResponse<AccountSummaryResponse>;
  pageParams: PageParams;
  setPageParams: (params: PageParams) => void;
  onActionSuccess: () => void;
}

export const AccountsTable: React.FC<AccountsTableProps> = ({
    data,
    pageParams,
    setPageParams,
    onActionSuccess,
}) => {
    const columns: Column<AccountSummaryResponse>[] = [
        { title: "Идентификатор", key: "id", align: "left", render: (value: string) => <CopyableId id={value} label="Идентификатор счёта" /> },
        { title: "Баланс", key: "balance", align: "right", render: (value: number) => `${value} BYN` },
        { title: "Действия", key: "actions", align: "center", render: (_: any, row: AccountSummaryResponse) => <AccountActionMenu accountId={row.id} onSuccess={onActionSuccess} /> },
    ];

    return (
        <ResponsiveTable
            columns={columns}
            data={data.content}
            totalElements={data.totalElements}
            pageParams={pageParams}
            setPageParams={setPageParams}
        />
  );
};