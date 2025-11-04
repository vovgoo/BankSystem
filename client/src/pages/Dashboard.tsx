import { useEffect, useState } from 'react';
import { Box, Heading, Text, SimpleGrid, Skeleton, Card } from '@chakra-ui/react';
import type { StatsResponse } from '../api';
import { statsService } from '../api';
import { EmptyState } from '@/components';
import { FiAlertCircle } from 'react-icons/fi';

type Item = {
  label: string;
  value: number;
  icon?: React.ReactNode;
};

export const Dashboard: React.FC = () => {
  const [data, setData] = useState<StatsResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(false);

  const fetchData = async () => {
    setIsLoading(true);
    setError(false);
    try {
      const res = await statsService.getOverview();
      setData(res);
    } catch (err) {
      setError(true);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const items: Item[] = [
    { label: 'Всего клиентов', value: data?.totalClients ?? 0 },
    { label: 'Клиентов с аккаунтами', value: data?.clientsWithAccounts ?? 0 },
    { label: 'Клиентов без аккаунтов', value: data?.clientsWithoutAccounts ?? 0 },
    { label: 'Всего аккаунтов', value: data?.totalAccounts ?? 0 },
    { label: 'Общий баланс', value: data?.totalBalance ?? 0 },
    { label: 'Средний баланс', value: data?.averageBalance ?? 0 },
    { label: 'Макс. баланс', value: data?.maxBalance ?? 0 },
    { label: 'Мин. баланс', value: data?.minBalance ?? 0 },
  ];

  const cardBg = '#114852';
  const textColor = '#E0E0E0';

  if (error) {
    return (
      <EmptyState
        icon={<FiAlertCircle size="30px" />}
        title="Не удалось загрузить статистику"
      />
    );
  }

  return (
    <Box p={8} color={textColor}>
      <Heading mb={8} color="white" fontWeight="600" letterSpacing={1}>
        Общая статистика
      </Heading>
      <SimpleGrid columns={{ base: 1, sm: 2, md: 4 }} gap={10}>
        {items.map((item, idx) => (
          <Card.Root
            key={idx}
            bg={cardBg}
            border={`1px solid ${cardBg}`}
            rounded="xl"
            shadow="md"
            borderWidth={2}
            borderColor="#0B262B"
          >
            <Card.Body>
              <Box mt={2}>
                <Text fontSize="xl" color={textColor}>
                  {item.label}
                </Text>
              </Box>
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
                <Text fontSize="3xl" fontWeight="bold">
                  {item.value.toLocaleString()}
                </Text>
              )}
            </Card.Footer>
          </Card.Root>
        ))}
      </SimpleGrid>
    </Box>
  );
};
