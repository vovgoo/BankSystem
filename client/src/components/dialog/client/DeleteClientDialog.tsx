import { useState } from "react";
import { Box, Button, Text } from "@chakra-ui/react";

import type { UUID } from "@api";
import { clientsService } from "@api";

import { BaseDialog } from "../base";
import { notifyTransaction } from "@utils";

type DeleteClientDialogProps = {
  clientId: UUID;
  onClose: () => void;
};

export const DeleteClientDialog: React.FC<DeleteClientDialogProps> = ({ clientId, onClose }) => {
  const [isLoading, setIsLoading] = useState(false);

  const handleDelete = async () => {
    setIsLoading(true);

    try {
      await clientsService.delete(clientId);
      notifyTransaction();
    } catch (error) {
      notifyTransaction(error);
    } finally {
      setIsLoading(false);
      onClose();
    }
  };

  return (
    <BaseDialog
      isOpen={true}
      onClose={onClose}
      title="Точно хотите удалить?"
      body={
        <Text color="gray.400">
          Вы собираетесь удалить клиента <strong className="text-white">{clientId}</strong>. Это действие нельзя будет отменить.
        </Text>
      }
      footer={
        <Box display="flex" justifyContent="flex-end" gap={2}>
          <Button colorScheme="red" loading={isLoading} onClick={handleDelete}>
            Удалить
          </Button>
          <Button onClick={onClose} variant="outline">
            Отмена
          </Button>
        </Box>
      }
    />
  );
};