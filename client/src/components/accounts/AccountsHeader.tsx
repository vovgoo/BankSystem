import React from "react";
import { Box, Flex, Heading, Text, Skeleton } from "@chakra-ui/react";
import { CopyableId, CreateAccountDialogButton, ClientDetailActionMenu } from "@components";
import type { ClientDetailsResponse } from "@api";

type AccountsHeaderProps = {
  client: ClientDetailsResponse | null;
  isLoading?: boolean;
  onActionSuccess: () => void;
};

export const AccountsHeader: React.FC<AccountsHeaderProps> = ({ client, isLoading, onActionSuccess }) => (
  <>
    <Flex justify="space-between" mb={10}>
      <Box>
        {isLoading ? (
          <Skeleton height="32px" width="325px" mb={4} />
        ) : (
          <CopyableId id={client?.id} label="Идентификатор клиента" color="gray.300" justify="flex-start" />
        )}

        {isLoading ? (
          <Skeleton height="60px" width="200px" mt={4} mb={2} />
        ) : (
          <Heading mt={10} mb={2} size="5xl" color="white">
            {client?.lastName}
          </Heading>
        )}

        {isLoading ? (
          <Skeleton height="21px" width="150px" />
        ) : (
          <Text mt={5} color="gray.300" fontSize="sm">
            {client?.phone}
          </Text>
        )}
      </Box>

      <Box>
        {isLoading ? (
          <Skeleton height="40px" width="156px" />
        ) : (
          <ClientDetailActionMenu clientId={client?.id} onSuccess={onActionSuccess} />
        )}
      </Box>  
    </Flex>

    <Flex mb={6} justify="space-between" alignItems="center">
      {isLoading ? (
        <Skeleton height="30px" width="147px" />
      ) : (
        <Heading color="white">Счета клиента</Heading>
      )}

      {isLoading ? (
        <Skeleton height="40px" width="150px" />
      ) : (
        <CreateAccountDialogButton clientId={client?.id} onSuccess={onActionSuccess} />
      )}
    </Flex>
  </>
);