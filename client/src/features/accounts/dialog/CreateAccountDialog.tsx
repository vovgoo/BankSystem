import { Text } from '@chakra-ui/react';
import type { UUID } from '@api';
import { ConfirmDialog } from '@components';
import { useCreateAccount } from '@/hooks';

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
  const mutation = useCreateAccount(clientId, () => {
    onSuccess?.();
    onClose();
  });

  const handleConfirm = (): void => {
    mutation.mutate(undefined);
  };

  return (
    <ConfirmDialog
      isOpen={isOpen}
      onClose={onClose}
      onConfirm={handleConfirm}
      title="Открыть счет"
      message={
        <Text>
          Вы собираетесь открыть новый счет для клиента{' '}
          <strong className="text-white">{clientId}</strong>.
        </Text>
      }
      confirmLabel="Открыть"
      confirmColorScheme="teal"
      isLoading={mutation.isPending}
    />
  );
};
