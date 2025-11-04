import { useState } from 'react';
import { Box, Button, HStack, Text, VStack } from '@chakra-ui/react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';

import { BaseDialog } from '../base';
import { DisabledInput, NumberInput, TextInput } from '@components';
import { transferSchema, type TransferFormData } from '@schemas';
import { accountsService, UUID } from '@api';
import { notifyTransaction } from '@utils';
import { LuInfo } from 'react-icons/lu';

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
  const [isLoading, setIsLoading] = useState(false);

  const {
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
    reset,
  } = useForm<TransferFormData>({
    resolver: zodResolver(transferSchema),
    defaultValues: { fromAccountId: accountId, toAccountId: '', amount: 0 },
  });

  const onSubmit = async (data: TransferFormData) => {
    setIsLoading(true);
    try {
      await accountsService.transfer(data);
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
      title="Перевод средств"
      body={
        <Box display="flex" flexDirection="column" gap={4}>
          <DisabledInput label="Текущий счет" value={accountId} />

          <TextInput
            label="Счет получателя"
            value={watch('toAccountId')}
            onChange={(val) => setValue('toAccountId', val)}
            error={errors.toAccountId?.message}
            placeholder="Введите ID счета получателя"
          />

          <NumberInput
            label="Сумма перевода"
            value={watch('amount')}
            onChange={(val) => setValue('amount', val)}
            error={errors.amount?.message}
            placeholder="0.00"
          />

          <Text fontSize="sm" color="gray.400">
            <LuInfo style={{ display: 'inline-block', verticalAlign: 'middle', marginRight: 7 }} />
            При переводе средств комиссия составляет 5% от суммы перевода
          </Text>

          <VStack align="flex-start">
            <Text color="white" fontWeight="bold">
              Комиссия:{' '}
              {(() => {
                const amount = parseFloat(String(watch('amount')));
                if (isNaN(amount) || amount <= 0) return '0.00';
                return (amount * 0.05).toFixed(2);
              })()}{' '}
              BYN
            </Text>

            <Text color="white" fontWeight="bold">
              Общая сумма:{' '}
              {(() => {
                const amount = parseFloat(String(watch('amount')));
                if (isNaN(amount) || amount <= 0) return '0.00';
                const commission = amount * 0.05;
                return (amount + commission).toFixed(2);
              })()}{' '}
              BYN
            </Text>
          </VStack>
        </Box>
      }
      footer={
        <Box display="flex" justifyContent="flex-end" gap={2}>
          <Button colorScheme="teal" loading={isLoading} onClick={handleSubmit(onSubmit)}>
            Перевести
          </Button>
        </Box>
      }
    />
  );
};
