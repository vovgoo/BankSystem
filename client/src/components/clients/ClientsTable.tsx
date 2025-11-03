import { ResponsiveTable, Column } from "@components";
import type { ClientListItem, PageParams, PageResponse } from "@api";
import { ClientActionMenu } from "@components";
import { formatPhone } from "@utils";

interface ClientsTableProps {
  data: PageResponse<ClientListItem>;
  pageParams: PageParams;
  setPageParams: (params: PageParams) => void;
  onActionSuccess: () => void;
}

export const ClientsTable: React.FC<ClientsTableProps> = ({ data, pageParams, setPageParams, onActionSuccess }) => {

  const columns: Column<ClientListItem>[] = [
    { title: "Идентификатор", key: "id", align: "left", render: (value: any) => <span>{value}</span> },
    { title: "Фамилия", key: "lastName", align: "left" },
    { title: "Телефон", key: "phone", align: "center", render: (value: any) => formatPhone(value) },
    { title: "Общий баланс", key: "totalBalance", align: "right", render: (value: any) => `${value.toLocaleString()} BYN` },
    { title: "Действия", key: "actions", align: "center", render: (_: any, row: ClientListItem) => (
      <ClientActionMenu clientId={row.id} onSuccess={onActionSuccess} />
    ) },
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