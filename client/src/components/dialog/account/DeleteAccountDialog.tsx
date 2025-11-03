import { useState } from "react";
import { Box, Button, Text } from "@chakra-ui/react";
import { BaseDialog } from "../base";
import { accountsService, UUID } from "@api";
import { notifyTransaction } from "@/utils";

type DeleteAccountDialogProps = {
  accountId: UUID;
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
};

export const DeleteAccountDialog: React.FC<DeleteAccountDialogProps> = ({
  accountId,
  isOpen,
  onClose,
  onSuccess,
}) => {
  const [isLoading, setIsLoading] = useState(false);

  const handleDelete = async () => {
    setIsLoading(true);
    try {
      await accountsService.delete(accountId);
      notifyTransaction();
      onSuccess?.();
    } catch (error) {
      notifyTransaction(error);
    } finally {
      setIsLoading(false);
      onClose();
    }
  };

  return (
    <BaseDialog
      isOpen={isOpen}
      onClose={onClose}
      title="Закрытие счета"
      body={
        <Text color="gray.400">
          Вы собираетесь закрыть счет <strong className="text-white">{accountId}</strong>. Это действие нельзя будет отменить.
        </Text>
      }
      footer={
        <Box display="flex" justifyContent="flex-end" gap={2}>
          <Button colorScheme="red" loading={isLoading} onClick={handleDelete}>
            Закрыть счет
          </Button>
        </Box>
      }
    />
  );
};