import React, { useEffect } from 'react';
import { Box, Button } from '@chakra-ui/react';
import { type UseFormReturn, type FieldValues } from 'react-hook-form';
import { BaseDialog } from './BaseDialog';

type FormDialogProps<T extends FieldValues> = {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  form: UseFormReturn<T>;
  onSubmit: (data: T) => void | Promise<void>;
  isLoading?: boolean;
  submitLabel?: string;
  submitColorScheme?: string;
  children: (form: UseFormReturn<T>) => React.ReactNode;
  footerActions?: React.ReactNode;
};

export const FormDialog = <T extends FieldValues>({
  isOpen,
  onClose,
  title,
  form,
  onSubmit,
  isLoading = false,
  submitLabel = 'Сохранить',
  submitColorScheme = 'teal',
  children,
  footerActions,
}: FormDialogProps<T>) => {
  const handleSubmit = form.handleSubmit((data) => {
    onSubmit(data);
  });

  useEffect(() => {
    if (isOpen) {
      form.reset();
    }
  }, [isOpen, form]);

  return (
    <BaseDialog
      isOpen={isOpen}
      onClose={onClose}
      title={title}
      body={
        <Box>
          <form onSubmit={handleSubmit} id="form-dialog-form">
            {children(form)}
          </form>
        </Box>
      }
      footer={
        <Box display="flex" justifyContent="flex-end" gap={2}>
          {footerActions}
          <Button
            type="submit"
            form="form-dialog-form"
            colorScheme={submitColorScheme}
            loading={isLoading}
          >
            {submitLabel}
          </Button>
        </Box>
      }
    />
  );
};
