import React from 'react';
import { Box } from '@chakra-ui/react';
import { Header } from '../header';
import { colors } from '@theme';

interface MainLayoutProps {
  children: React.ReactNode;
}

export const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  return (
    <Box minH="100vh" display="flex" flexDirection="column" bg={colors.background.canvas}>
      <Header />
      <Box flex={1} display="flex" flexDirection="column" overflow="auto">
        {children}
      </Box>
    </Box>
  );
};
