import { memo, useCallback } from 'react';
import { Box, IconButton, Text } from '@chakra-ui/react';
import { FiCopy } from 'react-icons/fi';
import { toaster } from '@components';
import type { UUID } from '@api';
import { logger } from '@utils';

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

export const CopyableId: React.FC<CopyableIdProps> = memo(
  ({ id, label, color = 'white', justify = 'space-between' }) => {
    const handleCopy = useCallback(async () => {
      if (!id) return;
      try {
        await navigator.clipboard.writeText(id);
        toaster.create({
          title: label ? `${label} скопирован` : 'Идентификатор скопирован',
          description: id,
        });
      } catch (err) {
        logger.error('Не удалось скопировать идентификатор', { error: err, id });
      }
    }, [id, label]);

    return (
      <Box gap={2} display="flex" alignItems="center" justifyContent={justify} color={color}>
        <Text fontSize="sm">{id}</Text>
        <IconButton aria-label="Скопировать ID" size="xs" variant="ghost" onClick={handleCopy}>
          <FiCopy />
        </IconButton>
      </Box>
    );
  }
);
