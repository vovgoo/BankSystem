import { VStack, Box, Text } from "@chakra-ui/react";
import { FiUsers } from "react-icons/fi";

export const ClientsEmpty: React.FC = () => (
  <VStack height="calc(100dvh - 282px)" width="100%" alignItems="center" justifyContent="center">
    <Box height="50px">
      <FiUsers size={30} />
    </Box>
    <Text>Клиенты не найдены</Text>
  </VStack>
);