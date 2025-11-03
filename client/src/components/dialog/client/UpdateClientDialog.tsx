import { useEffect, useState } from "react";

import { Button, Box } from "@chakra-ui/react";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { CenteredSpinner, TextInput, PhoneInput } from "@components";
import { BaseDialog } from "../base";

import { updateClientSchema } from "@schemas";
import type { UpdateClientFormData } from "@schemas";

import { clientsService } from "@api";
import type { UUID, GetClientResponse } from "@api";

import { notifyTransaction } from "@utils";

type UpdateClientDialogProps = {
  clientId: UUID;
  onClose: () => void;
  onSuccess?: () => void;
};

export const UpdateClientDialog: React.FC<UpdateClientDialogProps> = ({ clientId, onClose, onSuccess }) => {
  const [loading, setLoading] = useState(true);
  const [isLoading, setIsLoading] = useState(false); 

  const { handleSubmit, reset, setValue, formState: { errors }, watch } = useForm<UpdateClientFormData>({
    resolver: zodResolver(updateClientSchema),
  });

  useEffect(() => {
    async function fetchClient() {
      try {
        setLoading(true);
        const data: GetClientResponse = await clientsService.getById(clientId, { page: 0, size: 1 });
        reset({
          id: clientId,
          lastName: data.lastName,
          phone: data.phone,
        });
      } finally {
        setLoading(false);
      }
    }
    fetchClient();
  }, [clientId, reset]);

  const onSubmit = async (data: UpdateClientFormData) => {
    setIsLoading(true);

    try {
      await clientsService.update(data);
      notifyTransaction();
      onSuccess?.();
    } catch (error) {
      notifyTransaction(error);
    } finally {
      setIsLoading(false);
      onClose();
    }
  };

  return (
    <BaseDialog
      isOpen={true}
      onClose={onClose}
      title={`Обновление клиента`}
      body={
        <Box>
          {loading ? (
            <CenteredSpinner/>
          ) : (
            <form onSubmit={handleSubmit(onSubmit)}>
              <Box mb={4}>
                <TextInput
                  label="Фамилия"
                  value={watch("lastName")}
                  onChange={(value) => setValue("lastName", value)}
                  error={errors.lastName?.message}
                  placeholder="Иванов"
                />
              </Box>

              <Box mb={4}>
                <PhoneInput
                  label="Телефон"
                  value={watch("phone")}
                  onChange={(value) => setValue("phone", value)}
                  error={errors.phone?.message}
                />
              </Box>
            </form>
          )}
        </Box>
      }
      footer={
        <Box display="flex" justifyContent="center" gap={2}>
          <Button type="submit" colorScheme="teal" loading={isLoading} onClick={handleSubmit(onSubmit)}>
            Изменить
          </Button>
        </Box>
      }
    />
  );
}