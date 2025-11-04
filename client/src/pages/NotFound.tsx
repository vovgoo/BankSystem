import { Box, Text, Heading, Button } from '@chakra-ui/react';
import React from 'react';
import { useNavigate } from 'react-router-dom';

export const NotFound: React.FC = () => {
  const navigate = useNavigate();

  return (
    <Box
      flex={1}
      display="flex"
      flexDirection="column"
      justifyContent="center"
      alignItems="center"
      height="100vh"
      textAlign="center"
      p={6}
      color="white"
    >
      <Heading size="6xl" mb={4}>
        404
      </Heading>
      <Text fontSize="xl" mb={6}>
        Страница не найдена
      </Text>
      <Button
        colorScheme="teal"
        size="lg"
        onClick={() => navigate('/')}
      >
        На главную
      </Button>
    </Box>
  );
};
