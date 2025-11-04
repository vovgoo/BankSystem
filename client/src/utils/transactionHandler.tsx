import { toaster } from '@components';
import { Box, Text } from '@chakra-ui/react';
import { formatDate } from './formatDate';

export const notifyTransaction = (error?: any) => {
  if (error) {
    const response = error.response?.data;
    toaster.error({
      title: 'Транзакция не была проведена',
      description: response ? (
        <>
          <Text>{response.message}</Text>
          <Box textAlign="right" fontSize="sm">
            {formatDate(response.timestamp)}
          </Box>
        </>
      ) : (
        'Ошибка сервиса. Попробуйте позже'
      ),
    });
    return;
  }

  toaster.success({ title: 'Транзакция прошла успешно' });
};
