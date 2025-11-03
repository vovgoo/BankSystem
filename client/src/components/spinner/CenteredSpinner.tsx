import { Box, Spinner } from "@chakra-ui/react";
import React from "react";

export const CenteredSpinner: React.FC = () => {
  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      width="100%"
      height="100%"
      py={8}
    >
      <Spinner />
    </Box>
  );
};