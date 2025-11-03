import { useState } from "react";
import { Box, Button } from "@chakra-ui/react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { BaseDialog } from "../base";
import { NumberInput } from "@components";
import { withdrawSchema, type WithdrawFormData } from "@schemas";
import { accountsService, UUID } from "@api";
import { notifyTransaction } from "@utils";

type WithdrawAccountDialogProps = {
  accountId: UUID;
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
};

export const WithdrawAccountDialog: React.FC<WithdrawAccountDialogProps> = ({
  accountId,
  isOpen,
  onClose,
  onSuccess,
}) => {
  const [isLoading, setIsLoading] = useState(false);

  const {
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
    reset,
  } = useForm<WithdrawFormData>({
    resolver: zodResolver(withdrawSchema),
    defaultValues: { accountId, amount: 0 },
  });

  const onSubmit = async (data: WithdrawFormData) => {
    setIsLoading(true);
    try {
      await accountsService.withdraw(data);
      notifyTransaction();
      onSuccess?.();
      reset();
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
      title="Снятие средств"
      body={
        <Box>
          <NumberInput
            label="Сумма снятия"
            value={watch("amount")}
            onChange={(val) => setValue("amount", val)}
            error={errors.amount?.message}
            placeholder="0.00"
          />
        </Box>
      }
      footer={
        <Box display="flex" justifyContent="flex-end" gap={2}>
          <Button colorScheme="red" loading={isLoading} onClick={handleSubmit(onSubmit)}>
            Снять
          </Button>
        </Box>
      }
    />
  );
};