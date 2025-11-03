import { useState } from "react";
import { Box, Button } from "@chakra-ui/react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { BaseDialog } from "../base";
import { NumberInput } from "@components";
import { depositSchema, type DepositFormData } from "@schemas";
import { accountsService, UUID } from "@api";
import { notifyTransaction } from "@utils";

type DepositAccountDialogProps = {
  accountId: UUID;
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
};

export const DepositAccountDialog: React.FC<DepositAccountDialogProps> = ({
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
  } = useForm<DepositFormData>({
    resolver: zodResolver(depositSchema),
    defaultValues: { accountId, amount: 0 },
  });

  const onSubmit = async (data: DepositFormData) => {
    setIsLoading(true);
    try {
      await accountsService.deposit(data);
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
      title="Пополнение счёта"
      body={
        <Box>
          <NumberInput
            label="Сумма пополнения"
            value={watch("amount")}
            onChange={(val) => setValue("amount", val)}
            error={errors.amount?.message}
            placeholder="0.00"
          />
        </Box>
      }
      footer={
        <Box display="flex" justifyContent="flex-end" gap={2}>
          <Button colorScheme="teal" loading={isLoading} onClick={handleSubmit(onSubmit)}>
            Пополнить
          </Button>
        </Box>
      }
    />
  );
};