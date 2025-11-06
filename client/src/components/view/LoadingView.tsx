import { memo } from 'react';
import { Box, Text, Spinner } from '@chakra-ui/react';

type LoaderProps = {
  title?: string;
  height?: number | string;
};

export const LoadingView: React.FC<LoaderProps> = memo(
  ({ title = 'Загрузка...', height = 100 }) => (
    <Box
      flex={1}
      display="flex"
      flexDirection="column"
      p={6}
      overflow="auto"
      justifyContent="center"
      alignItems="center"
      gap={4}
    >
      <Box height={height} display="flex" justifyContent="center" alignItems="center">
        <Spinner size="lg" />
      </Box>
      <Text color="gray.400" fontSize="md">
        {title}
      </Text>
    </Box>
  )
);
