import { Box, Text, Button } from "@chakra-ui/react";
import { FiAlertTriangle } from "react-icons/fi";

type ClientsErrorProps = {
  onRetry?: () => void;
};

export const LoadingError: React.FC<ClientsErrorProps> = ({ onRetry }) => (
  <Box gap={5} flex={1} display="flex" flexDirection="column" p={6} overflow="auto" justifyContent="center" alignItems="center">
    <Box color="red.400">
      <FiAlertTriangle size={36} />
    </Box>
    <Text fontSize="md" color="white">
      Ошибка соединения с сервером
    </Text>
    <Text fontSize="sm" color="gray.500">
      Проверьте подключение к интернету и попробуйте снова
    </Text>
    {onRetry && (
      <Button colorScheme="teal" size="sm" onClick={onRetry}>
        Повторить попытку
      </Button>
    )}
  </Box>
);