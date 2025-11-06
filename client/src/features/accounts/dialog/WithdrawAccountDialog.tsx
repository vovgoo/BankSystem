import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { FormDialog, NumberInput } from '@components';
import { withdrawSchema, type WithdrawFormData } from '@schemas';
import type { UUID } from '@api';
import { useWithdrawAccount } from '@/hooks';

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
  const mutation = useWithdrawAccount(() => {
    onSuccess?.();
    onClose();
  });

  const form = useForm<WithdrawFormData>({
    resolver: zodResolver(withdrawSchema),
    defaultValues: { accountId, amount: 0 },
  });

  useEffect(() => {
    if (isOpen) {
      form.reset({ accountId, amount: 0 });
    }
  }, [isOpen, accountId, form]);

  const handleSubmit = (data: WithdrawFormData): void => {
    mutation.mutate(data);
  };

  return (
    <FormDialog
      isOpen={isOpen}
      onClose={onClose}
      title="Снятие средств"
      form={form}
      onSubmit={handleSubmit}
      isLoading={mutation.isPending}
      submitLabel="Снять"
      submitColorScheme="red"
    >
      {(form) => (
        <NumberInput
          label="Сумма снятия"
          value={form.watch('amount')}
          onChange={(val) => form.setValue('amount', val)}
          error={form.formState.errors.amount?.message}
          placeholder="0.00"
        />
      )}
    </FormDialog>
  );
};
