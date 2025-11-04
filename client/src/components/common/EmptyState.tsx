import { Box, Text } from '@chakra-ui/react';
import React from 'react';

type EmptyStateProps = {
  icon: React.ReactNode;
  title: string;
};

export const EmptyState: React.FC<EmptyStateProps> = ({ icon, title }) => (
  <Box
    flex={1}
    display="flex"
    flexDirection="column"
    p={6}
    overflow="auto"
    justifyContent="center"
    alignItems="center"
  >
    <Box height="50px">{icon}</Box>
    <Text>{title}</Text>
  </Box>
);
