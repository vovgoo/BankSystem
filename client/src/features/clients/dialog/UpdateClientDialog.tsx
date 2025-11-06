import { useEffect, useState } from 'react';
import { Box, Spinner } from '@chakra-ui/react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { FormDialog, TextInput, PhoneInput } from '@components';
import { updateClientSchema } from '@schemas';
import type { UpdateClientFormData } from '@schemas';
import type { UUID } from '@api';
import { useUpdateClient, useClient } from '@/hooks';

type UpdateClientDialogProps = {
  clientId: UUID;
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
};

export const UpdateClientDialog: React.FC<UpdateClientDialogProps> = ({
  clientId,
  isOpen,
  onClose,
  onSuccess,
}) => {
  const [isReady, setIsReady] = useState(false);
  const mutation = useUpdateClient(() => {
    onSuccess?.();
    onClose();
  });

  const { data: clientData, isLoading: isFetching } = useClient(
    clientId,
    { page: 0, size: 1 },
    {
      enabled: isOpen,
    }
  );

  const form = useForm<UpdateClientFormData>({
    resolver: zodResolver(updateClientSchema),
  });

  useEffect(() => {
    if (clientData) {
      form.reset({
        id: clientData.id,
        lastName: clientData.lastName,
        phone: clientData.phone,
      });
      setIsReady(true);
    }
  }, [clientData, form]);

  const handleSubmit = (data: UpdateClientFormData): void => {
    mutation.mutate(data);
  };

  if (isFetching || !isReady) {
    return (
      <FormDialog
        isOpen={isOpen}
        onClose={onClose}
        title="Обновление клиента"
        form={form}
        onSubmit={handleSubmit}
        isLoading={mutation.isPending}
        submitLabel="Изменить"
      >
        {() => (
          <Box display="flex" justifyContent="center" alignItems="center" py={8}>
            <Spinner />
          </Box>
        )}
      </FormDialog>
    );
  }

  return (
    <FormDialog
      isOpen={isOpen}
      onClose={onClose}
      title="Обновление клиента"
      form={form}
      onSubmit={handleSubmit}
      isLoading={mutation.isPending}
      submitLabel="Изменить"
    >
      {(form) => (
        <>
          <Box mb={4}>
            <TextInput
              label="Фамилия"
              value={form.watch('lastName')}
              onChange={(value) => form.setValue('lastName', value)}
              error={form.formState.errors.lastName?.message}
              placeholder="Иванов"
            />
          </Box>
          <Box mb={4}>
            <PhoneInput
              label="Телефон"
              value={form.watch('phone')}
              onChange={(value) => form.setValue('phone', value)}
              error={form.formState.errors.phone?.message}
            />
          </Box>
        </>
      )}
    </FormDialog>
  );
};
