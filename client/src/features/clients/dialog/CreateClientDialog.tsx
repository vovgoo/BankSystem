import { Box } from '@chakra-ui/react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { FormDialog, TextInput, PhoneInput } from '@components';
import { createClientSchema } from '@schemas';
import type { CreateClientFormData } from '@schemas';
import { useCreateClient } from '@/hooks';

type CreateClientDialogProps = {
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
};

export const CreateClientDialog: React.FC<CreateClientDialogProps> = ({
  isOpen,
  onClose,
  onSuccess,
}) => {
  const mutation = useCreateClient(() => {
    onSuccess?.();
    onClose();
  });

  const form = useForm<CreateClientFormData>({
    resolver: zodResolver(createClientSchema),
    defaultValues: { lastName: '', phone: '+375 ' },
  });

  const handleSubmit = (data: CreateClientFormData): void => {
    mutation.mutate(data);
  };

  return (
    <FormDialog
      isOpen={isOpen}
      onClose={onClose}
      title="Создать клиента"
      form={form}
      onSubmit={handleSubmit}
      isLoading={mutation.isPending}
      submitLabel="Сохранить"
    >
      {(form) => (
        <>
          <Box mb={4}>
            <TextInput
              label="Фамилия"
              value={form.watch('lastName')}
              onChange={(val) => form.setValue('lastName', val)}
              error={form.formState.errors.lastName?.message}
              placeholder="Иванов"
            />
          </Box>
          <Box mb={4}>
            <PhoneInput
              label="Телефон"
              value={form.watch('phone')}
              onChange={(val) => form.setValue('phone', val)}
              error={form.formState.errors.phone?.message}
            />
          </Box>
        </>
      )}
    </FormDialog>
  );
};
