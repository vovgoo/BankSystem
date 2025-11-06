import React from 'react';
import { Box, Flex, Heading, Text, Skeleton } from '@chakra-ui/react';
import { CopyableId } from '@components';
import type { ClientDetailsResponse } from '@api';
import { formatPhone } from '@/utils';
import { ClientDetailActionMenu } from '@components';

type ClientDetailsProps = {
  client: ClientDetailsResponse | null;
  isLoading?: boolean;
  onActionSuccess: () => void;
};

export const ClientDetailsHeader: React.FC<ClientDetailsProps> = ({
  client,
  isLoading,
  onActionSuccess,
}) => (
  <>
    <Flex justify="space-between" mb={10}>
      <Box>
        {isLoading || !client ? (
          <Skeleton height="32px" width="325px" mb={4} />
        ) : (
          <CopyableId
            id={client.id}
            label="Идентификатор клиента"
            color="gray.300"
            justify="flex-start"
          />
        )}

        {isLoading || !client ? (
          <Skeleton height="60px" width="200px" mt={4} mb={2} />
        ) : (
          <Heading mt={10} mb={2} size="5xl" color="white">
            {client.lastName}
          </Heading>
        )}

        {isLoading || !client ? (
          <Skeleton height="21px" width="150px" />
        ) : (
          <Text mt={5} color="gray.300" fontSize="sm">
            {formatPhone(client.phone)}
          </Text>
        )}
      </Box>

      <Box>
        {isLoading || !client ? (
          <Skeleton height="40px" width="156px" />
        ) : (
          <ClientDetailActionMenu clientId={client.id} onSuccess={onActionSuccess} />
        )}
      </Box>
    </Flex>
  </>
);
