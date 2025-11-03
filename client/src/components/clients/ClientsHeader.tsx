import { Flex, HStack, Input, Heading } from "@chakra-ui/react";
import { CreateClientDialog } from "@components";

interface ClientsHeaderProps {
  search: string;
  onSearchChange: (val: string) => void;
  onCreateSuccess: () => void;
}

export const ClientsHeader: React.FC<ClientsHeaderProps> = ({
  search,
  onSearchChange,
  onCreateSuccess,
}) => {
  return (
    <>
      <Heading mb={4} color="white">Список клиентов</Heading>
      <Flex mb={6} justify="space-between" alignItems="center">
        <Input
          width="20%"
          placeholder="Поиск по фамилии"
          onChange={(e) => onSearchChange(e.target.value)}
          value={search}
        />
        <HStack gap={3}>
          <CreateClientDialog onSuccess={onCreateSuccess} />
        </HStack>
      </Flex>
    </>
  );
};