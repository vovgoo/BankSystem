import React, { useEffect, useState, useCallback } from "react";
import { Box } from "@chakra-ui/react";
import { useParams } from "react-router-dom";
import { clientsService } from "@api";
import type { ClientDetailsResponse, PageParams, PageResponse, AccountSummaryResponse } from "@api";
import { AccountsHeader, AccountsTable, EmptyState, LoadingError, PageLoader } from "@components";
import { FiCreditCard } from "react-icons/fi";

export const ClientDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [client, setClient] = useState<ClientDetailsResponse | null>(null);
  const [accountsData, setAccountsData] = useState<PageResponse<AccountSummaryResponse> | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(false);
  const [pageParams, setPageParams] = useState<PageParams>({ page: 0, size: 10 });

  const fetchClient = useCallback(async () => {
    if (!id) return;
    setIsLoading(true);
    setError(false);
    try {
      const res = await clientsService.getById(id, pageParams);
      setClient(res);
      setAccountsData(res.accounts);
    } catch (err) {
      console.error(err);
      setError(true);
    } finally {
      setIsLoading(false);
    }
  }, [id, pageParams]);

  useEffect(() => {
    fetchClient();
  }, [fetchClient]);

  const onActionSuccess = () => {
    fetchClient();
  };

  return (
    <Box flex={1} display="flex" flexDirection="column" p={6} overflow="auto">
      <AccountsHeader client={client} isLoading={isLoading} onActionSuccess={onActionSuccess} />

      {error ? (
        <LoadingError onRetry={fetchClient} />
      ) : isLoading || !accountsData ? (
        <PageLoader title="Загружаем счета клиента" />
      ) : accountsData.content && accountsData.content.length > 0 ? (
        <AccountsTable
          data={accountsData}
          pageParams={pageParams}
          setPageParams={setPageParams}
          onActionSuccess={onActionSuccess}
        />
      ) : (
        <EmptyState icon={<FiCreditCard size="30px" />} title="Счета не найдены" />
      )}
    </Box>
  );
};