import { useState, useEffect } from 'react';
import { Flex, Image, Text, Tabs, Box } from '@chakra-ui/react';
import { useNavigate, useLocation } from 'react-router-dom';
import { AppRoutes } from '../../routes';

export const Header = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [activeTab, setActiveTab] = useState<'stats' | 'manage'>('stats');

  useEffect(() => {
    if (location.pathname === AppRoutes.DASHBOARD) setActiveTab('stats');
    else if (location.pathname === AppRoutes.CLIENTS) setActiveTab('manage');
  }, [location.pathname]);

  const handleClick = (tab: 'stats' | 'manage') => {
    setActiveTab(tab);
    if (tab === 'stats') navigate(AppRoutes.DASHBOARD);
    else navigate(AppRoutes.CLIENTS);
  };

  return (
    <Flex align="center" justify="space-between" px="6" py="8" color="#E0E0E0" position="relative">
      <Flex align="center" gap="3">
        <Image src="/img/logo.png" alt="logo" boxSize="30px" />
        <Text fontWeight="500" letterSpacing={2}>
          BankSystem
        </Text>
      </Flex>

      <Box position="absolute" left="50%" transform="translateX(-50%)">
        <Tabs.Root value={activeTab} variant="plain">
          <Tabs.List
            bg="#114852"
            rounded="l3"
            p="1"
            gap="2"
            alignItems="center"
            justifyContent="center"
          >
            <Tabs.Trigger
              value="stats"
              color="white"
              _selected={{ color: '#00B1B1' }}
              rounded="l2"
              px="4"
              py="2"
              fontWeight="500"
              onClick={() => handleClick('stats')}
            >
              Статистика
            </Tabs.Trigger>

            <Tabs.Trigger
              value="manage"
              color="white"
              _selected={{ color: '#00B1B1' }}
              rounded="l2"
              px="4"
              py="2"
              fontWeight="500"
              onClick={() => handleClick('manage')}
            >
              Управление
            </Tabs.Trigger>

            <Tabs.Indicator bg="#0B262B" borderRadius={3} transition="all 0.3s" />
          </Tabs.List>
        </Tabs.Root>
      </Box>
    </Flex>
  );
};
