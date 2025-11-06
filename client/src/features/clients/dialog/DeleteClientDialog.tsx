import type { UUID } from '@api';
import { ConfirmDialog } from '@components';
import { useDeleteClient } from '@/hooks';

type DeleteClientDialogProps = {
  clientId: UUID;
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
};

export const DeleteClientDialog: React.FC<DeleteClientDialogProps> = ({
  clientId,
  isOpen,
  onClose,
  onSuccess,
}) => {
  const mutation = useDeleteClient(() => {
    onSuccess?.();
    onClose();
  });

  const handleConfirm = () => {
    mutation.mutate(clientId);
  };

  return (
    <ConfirmDialog
      isOpen={isOpen}
      onClose={onClose}
      onConfirm={handleConfirm}
      title="Точно хотите удалить?"
      message={
        <>
          Вы собираетесь удалить клиента <strong className="text-white">{clientId}</strong>. Это
          действие нельзя будет отменить.
        </>
      }
      confirmLabel="Удалить"
      isLoading={mutation.isPending}
    />
  );
};
