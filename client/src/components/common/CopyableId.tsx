import React from 'react';
import { Box, IconButton, Text } from '@chakra-ui/react';
import { FiCopy } from 'react-icons/fi';
import { toaster } from '@components';
import { UUID } from '@api';

type CopyableIdProps = {
  id: UUID;
  label?: string;
  color?: string;
  justify?:
    | 'flex-start'
    | 'flex-end'
    | 'center'
    | 'space-between'
    | 'space-around'
    | 'space-evenly';
};

export const CopyableId: React.FC<CopyableIdProps> = ({
  id,
  label,
  color = 'white',
  justify = 'space-between',
}) => {
  const handleCopy = async () => {
    if (!id) return;
    try {
      await navigator.clipboard.writeText(id);
      toaster.create({
        title: label ? `${label} скопирован` : 'Идентификатор скопирован',
        description: id,
      });
    } catch (err) {
      console.error('Не удалось скопировать:', err);
    }
  };

  return (
    <Box gap={2} display="flex" alignItems="center" justifyContent={justify} color={color}>
      <Text fontSize="sm">{id}</Text>
      <IconButton aria-label="Скопировать ID" size="xs" variant="ghost" onClick={handleCopy}>
        <FiCopy />
      </IconButton>
    </Box>
  );
};
