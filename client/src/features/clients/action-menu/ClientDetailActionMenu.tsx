import React, { useState, useCallback } from 'react';
import { FiEdit2, FiTrash2, FiMoreHorizontal } from 'react-icons/fi';
import { UpdateClientDialog, DeleteClientDialog } from '../dialog';
import type { UUID } from '@api';
import { BaseActionMenu } from '@components';

export const ClientDetailDialogType = {
  UPDATE: 'update',
  DELETE: 'delete',
} as const;

export type ClientDetailDialogType =
  (typeof ClientDetailDialogType)[keyof typeof ClientDetailDialogType];

type ClientDetailActionMenuProps = {
  clientId: UUID;
  onSuccess?: () => void;
};

type ClientDetailDialogComponent = React.ComponentType<{
  clientId: UUID;
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
}>;

const DIALOG_COMPONENTS: Record<ClientDetailDialogType, ClientDetailDialogComponent> = {
  update: UpdateClientDialog,
  delete: DeleteClientDialog,
};

export const ClientDetailActionMenu: React.FC<ClientDetailActionMenuProps> = ({
  clientId,
  onSuccess,
}) => {
  const [openDialog, setOpenDialog] = useState<ClientDetailDialogType | null>(null);
  const closeDialog = useCallback(() => setOpenDialog(null), []);

  const menuItems = [
    {
      value: 'edit',
      icon: <FiEdit2 />,
      label: 'Изменить',
      onSelect: () => setOpenDialog(ClientDetailDialogType.UPDATE),
    },
    {
      value: 'delete',
      icon: <FiTrash2 />,
      label: 'Удалить',
      onSelect: () => setOpenDialog(ClientDetailDialogType.DELETE),
    },
  ];

  const DialogComponent = openDialog ? DIALOG_COMPONENTS[openDialog] : null;

  return (
    <>
      <BaseActionMenu items={menuItems} triggerIcon={<FiMoreHorizontal />} />
      {DialogComponent && (
        <DialogComponent
          clientId={clientId}
          isOpen={true}
          onClose={closeDialog}
          onSuccess={onSuccess}
        />
      )}
    </>
  );
};
