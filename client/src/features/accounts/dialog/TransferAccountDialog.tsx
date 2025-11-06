import { useEffect } from 'react';
import { Box, Text, VStack } from '@chakra-ui/react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { FormDialog, DisabledInput, NumberInput, TextInput } from '@components';
import { transferSchema, type TransferFormData } from '@schemas';
import type { UUID } from '@api';
import { LuInfo } from 'react-icons/lu';
import { useTransferAccount } from '@/hooks';
import { formatCurrency } from '@utils';
import { COMMISSION_RATE } from '@/constants';

type TransferAccountDialogProps = {
  accountId: UUID;
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
};

export const TransferAccountDialog: React.FC<TransferAccountDialogProps> = ({
  accountId,
  isOpen,
  onClose,
  onSuccess,
}) => {
  const mutation = useTransferAccount(() => {
    onSuccess?.();
    onClose();
  });

  const form = useForm<TransferFormData>({
    resolver: zodResolver(transferSchema),
    defaultValues: { fromAccountId: accountId, toAccountId: '', amount: 0 },
  });

  useEffect(() => {
    if (isOpen) {
      form.reset({ fromAccountId: accountId, toAccountId: '', amount: 0 });
    }
  }, [isOpen, accountId, form]);

  const handleSubmit = (data: TransferFormData): void => {
    mutation.mutate(data);
  };

  const amount = form.watch('amount');
  const amountValue = parseFloat(String(amount)) || 0;
  const commission = amountValue > 0 ? amountValue * COMMISSION_RATE : 0;
  const total = amountValue + commission;

  return (
    <FormDialog
      isOpen={isOpen}
      onClose={onClose}
      title="Перевод средств"
      form={form}
      onSubmit={handleSubmit}
      isLoading={mutation.isPending}
      submitLabel="Перевести"
    >
      {(form) => (
        <Box display="flex" flexDirection="column" gap={4}>
          <DisabledInput label="Текущий счет" value={accountId} />

          <TextInput
            label="Счет получателя"
            value={form.watch('toAccountId')}
            onChange={(val) => form.setValue('toAccountId', val)}
            error={form.formState.errors.toAccountId?.message}
            placeholder="Введите ID счета получателя"
          />

          <NumberInput
            label="Сумма перевода"
            value={form.watch('amount')}
            onChange={(val) => form.setValue('amount', val)}
            error={form.formState.errors.amount?.message}
            placeholder="0.00"
          />

          <Text fontSize="sm" color="gray.400">
            <LuInfo style={{ display: 'inline-block', verticalAlign: 'middle', marginRight: 7 }} />
            При переводе средств комиссия составляет {COMMISSION_RATE * 100}% от суммы перевода
          </Text>

          <VStack align="flex-start">
            <Text color="white" fontWeight="bold">
              Комиссия: {formatCurrency(commission)}
            </Text>
            <Text color="white" fontWeight="bold">
              Общая сумма: {formatCurrency(total)}
            </Text>
          </VStack>
        </Box>
      )}
    </FormDialog>
  );
};
