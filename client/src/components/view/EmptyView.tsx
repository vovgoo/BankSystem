import { memo } from 'react';
import { Box, Text } from '@chakra-ui/react';

type EmptyViewProps = {
  icon: React.ReactNode;
  title: string;
};

export const EmptyView: React.FC<EmptyViewProps> = memo(({ icon, title }) => (
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
    <Box height="50px" color="gray.400">
      {icon}
    </Box>
    <Text color="gray.400" fontSize="md">
      {title}
    </Text>
  </Box>
));
