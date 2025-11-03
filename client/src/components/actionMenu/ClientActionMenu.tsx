import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FiEye, FiEdit2, FiTrash2, FiMoreHorizontal } from "react-icons/fi";
import { DeleteClientDialog, UpdateClientDialog } from "@components";
import { AppRoutes } from "@/routes";
import { BaseMenu } from "./BaseMenu"; 

type ClientActionsMenuProps = {
  clientId: string;
  onSuccess?: () => void;
};

export const ClientDialogType = {
  UPDATE: "update",
  DELETE: "delete",
} as const;

type ClientDialogType = (typeof ClientDialogType)[keyof typeof ClientDialogType];

export const ClientActionMenu: React.FC<ClientActionsMenuProps> = ({ clientId, onSuccess }) => {
  const navigate = useNavigate();
  const [openDialog, setOpenDialog] = useState<ClientDialogType | null>(null);

  const onView = () => {
    const path = AppRoutes.CLIENT_DETAILS.replace(":id", clientId);
    navigate(path);
  };

  const onEdit = () => setOpenDialog(ClientDialogType.UPDATE);
  const onDelete = () => setOpenDialog(ClientDialogType.DELETE);

  const menuItems = [
    { value: "view", icon: <FiEye />, label: "Подробнее", onSelect: onView },
    { value: "edit", icon: <FiEdit2 />, label: "Изменить", onSelect: onEdit },
    { value: "delete", icon: <FiTrash2 />, label: "Удалить", onSelect: onDelete },
  ];

  return (
    <>
      <BaseMenu items={menuItems} triggerIcon={<FiMoreHorizontal />} />

      {openDialog === ClientDialogType.UPDATE && (
        <UpdateClientDialog clientId={clientId} onClose={() => setOpenDialog(null)} onSuccess={onSuccess} />
      )}
      {openDialog === ClientDialogType.DELETE && (
        <DeleteClientDialog clientId={clientId} onClose={() => setOpenDialog(null)} onSuccess={onSuccess} />
      )}
    </>
  );
};