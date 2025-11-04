import { Box, Text } from '@chakra-ui/react';
import { CenteredSpinner } from '@components';

type LoaderProps = {
  title?: string;
  height?: number | string;
};

export const PageLoader: React.FC<LoaderProps> = ({ title = 'Загрузка...', height = 100 }) => (
  <Box
    flex={1}
    display="flex"
    flexDirection="column"
    p={6}
    overflow="auto"
    justifyContent="center"
    alignItems="center"
  >
    <Box height={height}>
      <CenteredSpinner />
    </Box>
    <Text>{title}</Text>
  </Box>
);
