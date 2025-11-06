import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { FormDialog, NumberInput } from '@components';
import { depositSchema, type DepositFormData } from '@schemas';
import type { UUID } from '@api';
import { useDepositAccount } from '@/hooks';

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
  const mutation = useDepositAccount(() => {
    onSuccess?.();
    onClose();
  });

  const form = useForm<DepositFormData>({
    resolver: zodResolver(depositSchema),
    defaultValues: { accountId, amount: 0 },
  });

  useEffect(() => {
    if (isOpen) {
      form.reset({ accountId, amount: 0 });
    }
  }, [isOpen, accountId, form]);

  const handleSubmit = (data: DepositFormData): void => {
    mutation.mutate(data);
  };

  return (
    <FormDialog
      isOpen={isOpen}
      onClose={onClose}
      title="Пополнение счёта"
      form={form}
      onSubmit={handleSubmit}
      isLoading={mutation.isPending}
      submitLabel="Пополнить"
    >
      {(form) => (
        <NumberInput
          label="Сумма пополнения"
          value={form.watch('amount')}
          onChange={(val) => form.setValue('amount', val)}
          error={form.formState.errors.amount?.message}
          placeholder="0.00"
        />
      )}
    </FormDialog>
  );
};
