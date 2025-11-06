import React from 'react';
import { Flex, Heading, Skeleton } from '@chakra-ui/react';
import { CreateAccountDialogButton } from '@components';
import type { ClientDetailsResponse } from '@api';

interface AccountsHeaderProps {
  client: ClientDetailsResponse;
  isLoading?: boolean;
  onActionSuccess: () => void;
}

export const AccountsHeader: React.FC<AccountsHeaderProps> = ({
  client,
  isLoading,
  onActionSuccess,
}) => {
  return (
    <Flex mb={6} justify="space-between" alignItems="center">
      {isLoading ? (
        <Skeleton height="30px" width="147px" />
      ) : (
        <Heading color="white">Счета клиента</Heading>
      )}

      {isLoading ? (
        <Skeleton height="40px" width="150px" />
      ) : (
        <CreateAccountDialogButton clientId={client.id} onSuccess={onActionSuccess} />
      )}
    </Flex>
  );
};
