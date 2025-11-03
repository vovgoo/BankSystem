import { useState } from "react";
import { Button, Box } from "@chakra-ui/react";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { BaseDialog } from "../base";
import { TextInput, PhoneInput } from "@components";

import { createClientSchema } from "@schemas";
import type { CreateClientFormData } from "@schemas";

import { clientsService } from "@api";

import { notifyTransaction } from "@utils";

export const CreateClientDialog: React.FC = () => {
    const [isOpen, setIsOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const { handleSubmit, reset, setValue, watch, formState: { errors } } = useForm<CreateClientFormData>({
    resolver: zodResolver(createClientSchema),
    defaultValues: { lastName: "", phone: "+375 " },
  });

  const onSubmit = async (data: CreateClientFormData) => {
    setIsLoading(true);
    try {
      await clientsService.create(data);
      notifyTransaction();
      reset();
      setIsOpen(false);
    } catch (error) {
      notifyTransaction(error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <Button colorScheme="teal" onClick={() => setIsOpen(true)}>
        Создать клиента
      </Button>

      <BaseDialog
        isOpen={isOpen}
        onClose={() => setIsOpen(false)}
        title="Создать клиента"
        body={
          <Box>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Box mb={4}>
                <TextInput
                  label="Фамилия"
                  value={watch("lastName")}
                  onChange={(val) => setValue("lastName", val)}
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
          </Box>
        }
        footer={
          <Box display="flex" justifyContent="center" gap={2}>
            <Button
              type="submit"
              colorScheme="teal"
              loading={isLoading}
              onClick={handleSubmit(onSubmit)}
            >
              Сохранить
            </Button>
          </Box>
        }
      />
    </>
  );
}