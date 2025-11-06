import React, { Component, type ReactNode } from 'react';
import { Box, Button, Heading, Text, VStack } from '@chakra-ui/react';
import { FiAlertTriangle, FiRefreshCw } from 'react-icons/fi';

interface ErrorBoundaryProps {
  children: ReactNode;
  fallback?: ReactNode;
}

interface ErrorBoundaryState {
  hasError: boolean;
  error: Error | null;
}

export class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  constructor(props: ErrorBoundaryProps) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error: Error): ErrorBoundaryState {
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo): void {
    if (import.meta.env.DEV) {
      import('@utils').then(({ logger }) => {
        logger.error('ErrorBoundary caught an error', { error, errorInfo });
      });
    }
  }

  handleReset = (): void => {
    this.setState({ hasError: false, error: null });
  };

  render(): ReactNode {
    if (this.state.hasError) {
      if (this.props.fallback) {
        return this.props.fallback;
      }

      return (
        <Box
          minH="100vh"
          display="flex"
          alignItems="center"
          justifyContent="center"
          p={6}
          bg="gray.900"
        >
          <VStack gap={6} maxW="md" textAlign="center">
            <Box color="red.400">
              <FiAlertTriangle size={64} />
            </Box>
            <Heading size="lg" color="white">
              Что-то пошло не так
            </Heading>
            <Text color="gray.400" fontSize="md">
              Произошла непредвиденная ошибка. Пожалуйста, попробуйте обновить страницу.
            </Text>
            {import.meta.env.DEV && this.state.error && (
              <Box p={4} bg="gray.800" rounded="md" maxW="full" overflow="auto" textAlign="left">
                <Text fontSize="sm" color="red.400" fontFamily="mono">
                  {this.state.error.toString()}
                </Text>
              </Box>
            )}
            <Button colorScheme="teal" size="lg" onClick={this.handleReset}>
              Обновить страницу
              <FiRefreshCw />
            </Button>
          </VStack>
        </Box>
      );
    }

    return this.props.children;
  }
}
