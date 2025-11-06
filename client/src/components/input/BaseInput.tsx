import React from 'react';
import { Box, Input, Text, type InputProps } from '@chakra-ui/react';

type BaseInputProps = InputProps & {
  label?: string;
  error?: string;
  containerProps?: React.ComponentProps<typeof Box>;
};

export const BaseInput: React.FC<BaseInputProps> = ({ label, error, containerProps, ...props }) => {
  return (
    <Box mb={4} {...containerProps}>
      {label && (
        <Text mb={1} fontSize="sm" color="gray.300">
          {label}
        </Text>
      )}
      <Input {...props} />
      {error && (
        <Text color="red.500" fontSize="sm" mt={1}>
          {error}
        </Text>
      )}
    </Box>
  );
};
