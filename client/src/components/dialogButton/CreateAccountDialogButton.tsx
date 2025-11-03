import React, { useState, useCallback } from "react";
import { Button } from "@chakra-ui/react";
import { FiPlusCircle } from "react-icons/fi";
import { CreateAccountDialog } from "@components";
import type { UUID } from "@api";

type CreateAccountDialogButtonProps = {
  clientId: UUID;
  onSuccess?: () => void;
};

export const CreateAccountDialogButton: React.FC<CreateAccountDialogButtonProps> = ({ clientId, onSuccess }) => {
  const [isOpen, setIsOpen] = useState(false);
  const openDialog = useCallback(() => setIsOpen(true), []);
  const closeDialog = useCallback(() => setIsOpen(false), []);

  return (
    <>
      <Button colorScheme="teal" onClick={openDialog}>
        Открыть счёт
        <FiPlusCircle />
      </Button>
      <CreateAccountDialog clientId={clientId} isOpen={isOpen} onClose={closeDialog} onSuccess={onSuccess} />
    </>
  );
};