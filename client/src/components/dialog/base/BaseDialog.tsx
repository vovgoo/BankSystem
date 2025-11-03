import { ReactNode } from "react";
import { Dialog, Portal, CloseButton } from "@chakra-ui/react";

type BaseDialogProps = {
  isOpen: boolean;
  onClose: () => void;
  title: string | ReactNode;
  body: ReactNode;
  footer?: ReactNode;
};

export const BaseDialog: React.FC<BaseDialogProps> = ({ isOpen, onClose, title, body, footer }) => {
  return (
    <Dialog.Root open={isOpen}>
      <Portal>
        <Dialog.Positioner>
          <Dialog.Content>
            {title && (
              <Dialog.Header>
                  <Dialog.Title>{title}</Dialog.Title>
                  <Dialog.CloseTrigger asChild>
                    <CloseButton size="sm" outline="none" onClick={onClose} />
                  </Dialog.CloseTrigger>
              </Dialog.Header>
            )}
            {body && <Dialog.Body>{body}</Dialog.Body>}
            {footer && <Dialog.Footer>{footer}</Dialog.Footer>}
          </Dialog.Content>
        </Dialog.Positioner>
      </Portal>
    </Dialog.Root>
  );
};