import { useMemo, useCallback } from 'react';
import { Box, Heading, Text, SimpleGrid, Skeleton, Card } from '@chakra-ui/react';
import type { StatsResponse } from '@api';
import { ErrorView } from '@components';
import { colors } from '@theme';
import { useStatsOverview } from '@/hooks';
import { formatCurrency } from '@utils';

interface StatItem {
  id: string;
  label: string;
  value: number;
  icon?: React.ReactNode;
}

const STAT_ITEMS: Omit<StatItem, 'value'>[] = [
  { id: 'total-clients', label: 'Всего клиентов' },
  { id: 'clients-with-accounts', label: 'Клиентов с аккаунтами' },
  { id: 'clients-without-accounts', label: 'Клиентов без аккаунтов' },
  { id: 'total-accounts', label: 'Всего аккаунтов' },
  { id: 'total-balance', label: 'Общий баланс' },
  { id: 'average-balance', label: 'Средний баланс' },
  { id: 'max-balance', label: 'Макс. баланс' },
  { id: 'min-balance', label: 'Мин. баланс' },
];

const getStatValue = (data: StatsResponse, id: string) => {
  switch (id) {
    case 'total-clients':
      return data.totalClients;
    case 'clients-with-accounts':
      return data.clientsWithAccounts;
    case 'clients-without-accounts':
      return data.clientsWithoutAccounts;
    case 'total-accounts':
      return data.totalAccounts;
    case 'total-balance':
      return data.totalBalance;
    case 'average-balance':
      return data.averageBalance;
    case 'max-balance':
      return data.maxBalance;
    case 'min-balance':
      return data.minBalance;
    default:
      return 0;
  }
};

export const Dashboard: React.FC = () => {
  const { data, isLoading, error, refetch } = useStatsOverview();

  const items: StatItem[] = useMemo(() => {
    if (!data) return [];
    return STAT_ITEMS.map((item) => ({
      ...item,
      value: getStatValue(data, item.id),
    }));
  }, [data]);

  const handleRetry = useCallback(() => {
    refetch();
  }, [refetch]);

  if (error) {
    return <ErrorView onRetry={handleRetry} />;
  }

  return (
    <Box p={8} color={colors.text.primary}>
      <Heading mb={8} color="white" fontWeight="600" letterSpacing={1}>
        Общая статистика
      </Heading>
      <SimpleGrid columns={{ base: 1, sm: 2, md: 4 }} gap={10}>
        {items.map((item) => (
          <Card.Root
            key={item.id}
            bg={colors.background.card}
            rounded="xl"
            shadow="md"
            borderWidth={2}
            borderColor={colors.background.border}
          >
            <Card.Body>
              <Text fontSize="xl" color={colors.text.primary}>
                {item.label}
              </Text>
            </Card.Body>

            <Card.Footer justifyContent="flex-end">
              {isLoading ? (
                <Skeleton
                  variant="shine"
                  height="45px"
                  width="45px"
                  css={{
                    '--start-color': '#0B262B',
                    '--end-color': '#081A1E',
                  }}
                />
              ) : (
                <Text fontSize="3xl" fontWeight="bold" color={colors.text.primary}>
                  {item.id.includes('balance')
                    ? formatCurrency(item.value)
                    : item.value.toLocaleString()}
                </Text>
              )}
            </Card.Footer>
          </Card.Root>
        ))}
      </SimpleGrid>
    </Box>
  );
};
