import { memo, useCallback } from 'react';
import { Box, Text, Button } from '@chakra-ui/react';
import { FiAlertTriangle } from 'react-icons/fi';
import { ICON_SIZE } from '@/constants';

type ErrorViewProps = {
  onRetry?: () => void;
};

export const ErrorView: React.FC<ErrorViewProps> = memo(({ onRetry }) => {
  const handleRetry = useCallback(() => {
    onRetry?.();
  }, [onRetry]);

  return (
    <Box
      gap={4}
      flex={1}
      display="flex"
      flexDirection="column"
      p={6}
      overflow="auto"
      justifyContent="center"
      alignItems="center"
    >
      <Box color="red.400">
        <FiAlertTriangle size={ICON_SIZE.MEDIUM} />
      </Box>
      <Text fontSize="lg" color="white" fontWeight="500">
        Ошибка соединения с сервером
      </Text>
      <Text fontSize="sm" color="gray.400" textAlign="center">
        Проверьте подключение к интернету и попробуйте снова
      </Text>
      {onRetry && (
        <Button colorScheme="teal" size="md" onClick={handleRetry} mt={2}>
          Повторить попытку
        </Button>
      )}
    </Box>
  );
});
