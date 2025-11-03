import React from "react";
import { Box, Input, Text, InputProps } from "@chakra-ui/react";

type BaseInputProps = InputProps & {
  label?: string;
  error?: string;
};

export const BaseInput: React.FC<BaseInputProps> = ({ label, error, ...props }) => {
  return (
    <Box mb={4}>
      {label && <Text mb={1} fontSize="sm">{label}</Text>}
      <Input {...props} />
      {error && <Text color="red.500" fontSize="sm">{error}</Text>}
    </Box>
  );
};