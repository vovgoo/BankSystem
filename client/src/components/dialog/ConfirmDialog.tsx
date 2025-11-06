import React from 'react';
import { Box, Button, Text } from '@chakra-ui/react';
import { BaseDialog } from './BaseDialog';

type ConfirmDialogProps = {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void | Promise<void>;
  title: string;
  message: React.ReactNode;
  confirmLabel?: string;
  cancelLabel?: string;
  isLoading?: boolean;
  confirmColorScheme?: string;
};

export const ConfirmDialog: React.FC<ConfirmDialogProps> = ({
  isOpen,
  onClose,
  onConfirm,
  title,
  message,
  confirmLabel = 'Подтвердить',
  cancelLabel = 'Отмена',
  isLoading = false,
  confirmColorScheme = 'red',
}) => {
  const handleConfirm = () => {
    onConfirm();
  };

  return (
    <BaseDialog
      isOpen={isOpen}
      onClose={onClose}
      title={title}
      body={<Text color="gray.400">{message}</Text>}
      footer={
        <Box display="flex" justifyContent="flex-end" gap={2}>
          <Button onClick={onClose} variant="outline" disabled={isLoading}>
            {cancelLabel}
          </Button>
          <Button colorScheme={confirmColorScheme} loading={isLoading} onClick={handleConfirm}>
            {confirmLabel}
          </Button>
        </Box>
      }
    />
  );
};
