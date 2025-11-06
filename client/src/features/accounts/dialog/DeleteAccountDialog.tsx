import type { UUID } from '@api';
import { ConfirmDialog } from '@components';
import { useDeleteAccount } from '@/hooks';

type DeleteAccountDialogProps = {
  accountId: UUID;
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
};

export const DeleteAccountDialog: React.FC<DeleteAccountDialogProps> = ({
  accountId,
  isOpen,
  onClose,
  onSuccess,
}) => {
  const mutation = useDeleteAccount(() => {
    onSuccess?.();
    onClose();
  });

  const handleConfirm = () => {
    mutation.mutate(accountId);
  };

  return (
    <ConfirmDialog
      isOpen={isOpen}
      onClose={onClose}
      onConfirm={handleConfirm}
      title="Закрытие счета"
      message={
        <>
          Вы собираетесь закрыть счет <strong className="text-white">{accountId}</strong>. Это
          действие нельзя будет отменить.
        </>
      }
      confirmLabel="Закрыть счет"
      isLoading={mutation.isPending}
    />
  );
};
