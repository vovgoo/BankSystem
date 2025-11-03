import React, { useState, useCallback } from "react";
import { FiEdit2, FiTrash2, FiMoreHorizontal } from "react-icons/fi";
import { UpdateClientDialog, DeleteClientDialog } from "@components";
import type { UUID } from "@api";
import { BaseMenu } from "./BaseMenu";

export const ClientDetailDialogType = {
  UPDATE: "update",
  DELETE: "delete",
} as const;

export type ClientDetailDialogType = (typeof ClientDetailDialogType)[keyof typeof ClientDetailDialogType];

type ClientDetailActionMenuProps = {
  clientId: UUID;
  onSuccess?: () => void;
};

const DIALOG_COMPONENTS: Record<ClientDetailDialogType, any> = {
  update: UpdateClientDialog,
  delete: DeleteClientDialog,
};

export const ClientDetailActionMenu: React.FC<ClientDetailActionMenuProps> = ({ clientId, onSuccess }) => {
  const [openDialog, setOpenDialog] = useState<ClientDetailDialogType | null>(null);
  const closeDialog = useCallback(() => setOpenDialog(null), []);

  const menuItems = [
    { value: "edit", icon: <FiEdit2 />, label: "Изменить", onSelect: () => setOpenDialog(ClientDetailDialogType.UPDATE) },
    { value: "delete", icon: <FiTrash2 />, label: "Удалить", onSelect: () => setOpenDialog(ClientDetailDialogType.DELETE) },
  ];

  const DialogComponent = openDialog ? DIALOG_COMPONENTS[openDialog] : null;

  return (
    <>
      <BaseMenu items={menuItems} triggerIcon={<FiMoreHorizontal />} />
      {DialogComponent && <DialogComponent clientId={clientId} isOpen={true} onClose={closeDialog} onSuccess={onSuccess} />}
    </>
  );
};