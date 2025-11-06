import { useMemo, memo } from 'react';
import { Flex, Image, Text, Box } from '@chakra-ui/react';
import { useNavigate, useLocation } from 'react-router-dom';
import { AppRoutes } from '@routes';
import { colors } from '@theme';

export const Header = memo(() => {
  const navigate = useNavigate();
  const location = useLocation();

  const activeTab = useMemo<'stats' | 'manage'>(() => {
    if (location.pathname === AppRoutes.DASHBOARD) return 'stats';
    if (location.pathname.startsWith(AppRoutes.CLIENTS)) return 'manage';
    return 'stats';
  }, [location.pathname]);

  return (
    <Flex
      align="center"
      justify="space-between"
      px="6"
      py="8"
      color={colors.text.primary}
      position="relative"
    >
      <Flex align="center" gap="3">
        <Image src="/img/logo.png" alt="logo" boxSize="30px" />
        <Text fontWeight="500" letterSpacing={2}>
          BankSystem
        </Text>
      </Flex>

      <Box
        position="absolute"
        left="50%"
        transform="translateX(-50%)"
        display="flex"
        gap="8"
        alignItems="center"
        px="4"
        py="2"
        rounded="l3"
      >
        <Text
          cursor="pointer"
          fontWeight={activeTab === 'stats' ? '700' : '500'}
          color={activeTab === 'stats' ? 'white' : colors.text.secondary}
          transition="all 0.2s"
          _hover={{ color: 'white' }}
          onClick={() => navigate(AppRoutes.DASHBOARD)}
        >
          Статистика
        </Text>

        <Text
          cursor="pointer"
          fontWeight={activeTab === 'manage' ? '700' : '500'}
          color={activeTab === 'manage' ? 'white' : colors.text.secondary}
          transition="all 0.2s"
          _hover={{ color: 'white' }}
          onClick={() => navigate(AppRoutes.CLIENTS)}
        >
          Управление
        </Text>
      </Box>
    </Flex>
  );
});
