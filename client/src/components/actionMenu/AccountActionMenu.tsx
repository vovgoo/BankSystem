import React, { useState, useCallback } from "react";
import { FiTrash2, FiMoreHorizontal, FiRepeat, FiArrowDown, FiPlusCircle } from "react-icons/fi";
import { DeleteAccountDialog, DepositAccountDialog, TransferAccountDialog, WithdrawAccountDialog } from "../dialog";
import { BaseMenu } from "./BaseMenu";
import type { UUID } from "@api";

export const AccountDialogType = {
  DEPOSIT: "deposit",
  WITHDRAW: "withdraw",
  TRANSFER: "transfer",
  DELETE: "delete",
} as const;

export type AccountDialogType = (typeof AccountDialogType)[keyof typeof AccountDialogType];

type AccountActionMenuProps = {
  accountId: UUID;
  onSuccess?: () => void;
};

const DIALOG_COMPONENTS: Record<AccountDialogType, any> = {
  delete: DeleteAccountDialog,
  deposit: DepositAccountDialog,
  transfer: TransferAccountDialog,
  withdraw: WithdrawAccountDialog,
};

export const AccountActionMenu: React.FC<AccountActionMenuProps> = ({ accountId, onSuccess }) => {
  const [openDialog, setOpenDialog] = useState<AccountDialogType | null>(null);

  const closeDialog = useCallback(() => setOpenDialog(null), []);

  const menuItems = [
    { value: "deposit", icon: <FiPlusCircle />, label: "Пополнить", onSelect: () => setOpenDialog(AccountDialogType.DEPOSIT) },
    { value: "withdraw", icon: <FiArrowDown />, label: "Снять", onSelect: () => setOpenDialog(AccountDialogType.WITHDRAW) },
    { value: "transfer", icon: <FiRepeat />, label: "Перевести", onSelect: () => setOpenDialog(AccountDialogType.TRANSFER) },
    { value: "delete", icon: <FiTrash2 />, label: "Закрыть счет", onSelect: () => setOpenDialog(AccountDialogType.DELETE) },
  ];

  const DialogComponent = openDialog ? DIALOG_COMPONENTS[openDialog] : null;

  return (
    <>
      <BaseMenu items={menuItems} triggerIcon={<FiMoreHorizontal />} />
      {DialogComponent && <DialogComponent accountId={accountId} isOpen={true} onClose={closeDialog} onSuccess={onSuccess} />}
    </>
  );
};