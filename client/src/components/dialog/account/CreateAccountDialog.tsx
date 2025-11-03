import { useState } from "react";
import { Box, Button, Text } from "@chakra-ui/react";
import { BaseDialog } from "../base";
import { accountsService } from "@api";
import { notifyTransaction } from "@utils";
import type { UUID } from "@api";

type CreateAccountDialogProps = {
  clientId: UUID;
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
};

export const CreateAccountDialog: React.FC<CreateAccountDialogProps> = ({
  clientId,
  isOpen,
  onClose,
  onSuccess,
}) => {
  const [isLoading, setIsLoading] = useState(false);

  const handleCreate = async () => {
    setIsLoading(true);
    try {
      await accountsService.create({ clientId });
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
      title="Открыть счет"
      body={
        <Box>
          <Text mb={4}>
            Вы собираетесь открыть новый счет для клиента <strong className="text-white">{clientId}</strong>.
          </Text>
        </Box>
      }
      footer={
        <Box display="flex" justifyContent="flex-end" gap={2}>
          <Button colorScheme="teal" loading={isLoading} onClick={handleCreate}>
            Открыть
          </Button>
        </Box>
      }
    />
  );
};