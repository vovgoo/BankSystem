import { memo, useCallback } from 'react';
import { Flex, HStack, Input, Heading } from '@chakra-ui/react';
import { CreateClientDialogButton } from '@components';

interface ClientsHeaderProps {
  search: string;
  onSearchChange: (val: string) => void;
  onCreateSuccess: () => void;
}

export const ClientsHeader: React.FC<ClientsHeaderProps> = memo(
  ({ search, onSearchChange, onCreateSuccess }) => {
    const handleInputChange = useCallback(
      (e: React.ChangeEvent<HTMLInputElement>) => {
        onSearchChange(e.target.value);
      },
      [onSearchChange]
    );

    return (
      <>
        <Heading mb={4} color="white">
          Список клиентов
        </Heading>
        <Flex mb={6} justify="space-between" alignItems="center">
          <Input
            width="20%"
            placeholder="Поиск по фамилии"
            onChange={handleInputChange}
            value={search}
          />
          <HStack gap={3}>
            <CreateClientDialogButton onSuccess={onCreateSuccess} />
          </HStack>
        </Flex>
      </>
    );
  }
);
