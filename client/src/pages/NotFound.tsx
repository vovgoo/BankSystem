import { Box, Text, Heading, Button } from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';
import { AppRoutes } from '@routes';

export const NotFound: React.FC = () => {
  const navigate = useNavigate();

  return (
    <Box
      flex={1}
      display="flex"
      flexDirection="column"
      justifyContent="center"
      alignItems="center"
      minH="100vh"
      textAlign="center"
      p={6}
      color="white"
    >
      <Heading size="6xl" mb={4}>
        404
      </Heading>
      <Text fontSize="xl" mb={6} color="gray.400">
        Страница не найдена
      </Text>
      <Button colorScheme="teal" size="lg" onClick={() => navigate(AppRoutes.DASHBOARD)}>
        На главную
      </Button>
    </Box>
  );
};
