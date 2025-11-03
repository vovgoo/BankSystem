import { Box, Text } from "@chakra-ui/react";
import { CenteredSpinner } from "@components";

export const ClientsLoader: React.FC = () => (
  <Box flex={1} display="flex" flexDirection="column" p={6} overflow="auto" justifyContent="center" alignItems="center">
    <Box height={100}>
      <CenteredSpinner />
    </Box>
    <Text>Загружаем пользователей</Text>
  </Box>
);