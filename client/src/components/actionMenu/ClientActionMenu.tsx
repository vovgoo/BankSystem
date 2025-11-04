import React, { useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { FiEye, FiEdit2, FiTrash2, FiMoreHorizontal } from 'react-icons/fi';
import { DeleteClientDialog, UpdateClientDialog } from '@components';
import type { UUID } from '@api';
import { AppRoutes } from '@/routes';
import { BaseMenu } from './BaseMenu';

export const ClientDialogType = {
  UPDATE: 'update',
  DELETE: 'delete',
} as const;

export type ClientDialogType = (typeof ClientDialogType)[keyof typeof ClientDialogType];

type ClientActionsMenuProps = {
  clientId: UUID;
  onSuccess?: () => void;
};

const DIALOG_COMPONENTS: Record<ClientDialogType, any> = {
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
      onSelect: () => navigate(AppRoutes.CLIENT_DETAILS.replace(':id', clientId)),
    },
    {
      value: 'edit',
      icon: <FiEdit2 />,
      label: 'Изменить',
      onSelect: () => setOpenDialog(ClientDialogType.UPDATE),
    },
    {
      value: 'delete',
      icon: <FiTrash2 />,
      label: 'Удалить',
      onSelect: () => setOpenDialog(ClientDialogType.DELETE),
    },
  ];

  const DialogComponent = openDialog ? DIALOG_COMPONENTS[openDialog] : null;

  return (
    <>
      <BaseMenu items={menuItems} triggerIcon={<FiMoreHorizontal />} />
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
