import { useState } from 'react';
import { Box, Button, Flex, Heading, Image, Link, Text, Code } from '@chakra-ui/react';
import reactLogo from './assets/react.svg';
import viteLogo from '/vite.svg';

function App() {
  const [count, setCount] = useState(0);

  return (
    <Box textAlign="center" fontFamily="sans-serif" p={6}>
      <Flex justify="center" gap={8} mb={6}>
        <Link href="https://vite.dev">
          <Image src={viteLogo} alt="Vite logo" boxSize="100px" mx="auto" />
        </Link>
        <Link href="https://react.dev">
          <Image src={reactLogo} alt="React logo" boxSize="100px" mx="auto" />
        </Link>
      </Flex>

      <Heading as="h1" size="2xl" mb={6}>
        Vite + React
      </Heading>

      <Box
        maxW="sm"
        mx="auto"
        p={6}
        borderWidth="1px"
        borderRadius="lg"
        boxShadow="md"
        mb={6}
      >
        <Button colorScheme="teal" size="lg" onClick={() => setCount(count + 1)} mb={4}>
          count is {count}
        </Button>
        <Text>
          Edit <Code>src/App.tsx</Code> and save to test HMR
        </Text>
      </Box>

      <Text fontSize="lg">
        Click on the Vite and React logos to learn more
      </Text>
    </Box>
  );
}

export default App;