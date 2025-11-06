import React, { useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { FiEye, FiEdit2, FiTrash2, FiMoreHorizontal } from 'react-icons/fi';
import { DeleteClientDialog, UpdateClientDialog } from '../dialog';
import type { UUID } from '@api';
import { createClientDetailsRoute } from '@/routes';
import { BaseActionMenu } from '@components';

export const ClientDialogType = {
  UPDATE: 'update',
  DELETE: 'delete',
} as const;

export type ClientDialogType = (typeof ClientDialogType)[keyof typeof ClientDialogType];

type ClientActionsMenuProps = {
  clientId: UUID;
  onSuccess?: () => void;
};

type ClientDialogComponent = React.ComponentType<{
  clientId: UUID;
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
}>;

const DIALOG_COMPONENTS: Record<ClientDialogType, ClientDialogComponent> = {
  update: UpdateClientDialog,
  delete: DeleteClientDialog,
};

export const ClientActionMenu: React.FC<ClientActionsMenuProps> = ({ clientId, onSuccess }) => {
  const navigate = useNavigate();
  const [openDialog, setOpenDialog] = useState<ClientDialogType | null>(null);

  const closeDialog = useCallback(() => setOpenDialog(null), []);

  const menuItems = [
    {
      value: 'view',
      icon: <FiEye />,
      label: 'Подробнее',
      onSelect: (): void => {
        void navigate(createClientDetailsRoute(clientId));
      },
    },
    {
      value: 'edit',
      icon: <FiEdit2 />,
      label: 'Изменить',
      onSelect: (): void => setOpenDialog(ClientDialogType.UPDATE),
    },
    {
      value: 'delete',
      icon: <FiTrash2 />,
      label: 'Удалить',
      onSelect: (): void => setOpenDialog(ClientDialogType.DELETE),
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
