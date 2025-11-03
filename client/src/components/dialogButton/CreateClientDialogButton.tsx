import React, { useState, useCallback } from "react";
import { Button } from "@chakra-ui/react";
import { FiUserPlus } from "react-icons/fi";
import { CreateClientDialog } from "@components";

type CreateClientDialogButtonProps = {
  onSuccess?: () => void;
};

export const CreateClientDialogButton: React.FC<CreateClientDialogButtonProps> = ({ onSuccess }) => {
  const [isOpen, setIsOpen] = useState(false);
  const openDialog = useCallback(() => setIsOpen(true), []);
  const closeDialog = useCallback(() => setIsOpen(false), []);

  return (
    <>
      <Button colorScheme="teal" onClick={openDialog}>
        Создать клиента
        <FiUserPlus />
      </Button>
      <CreateClientDialog isOpen={isOpen} onClose={closeDialog} onSuccess={onSuccess} />
    </>
  );
};