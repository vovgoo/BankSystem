package com.vovgoo.BankSystem.service.impl;

import com.vovgoo.BankSystem.dto.stats.StatsResponse;
import com.vovgoo.BankSystem.entity.Account;
import com.vovgoo.BankSystem.entity.Client;
import com.vovgoo.BankSystem.repository.AccountRepository;
import com.vovgoo.BankSystem.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("StatsServiceImpl — проверка статистики по клиентам и счетам")
class StatsServiceImplTest {

    @Autowired private StatsServiceImpl statsService;
    @Autowired private ClientRepository clientRepository;
    @Autowired private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        Client clientWithAccounts = clientRepository.save(new Client("Ivanov", "+375291234567"));

        Account acc1 = new Account(clientWithAccounts);
        acc1.deposit(new BigDecimal("100.50"));
        Account acc2 = new Account(clientWithAccounts);
        acc2.deposit(new BigDecimal("200.75"));
        accountRepository.saveAll(List.of(acc1, acc2));

        clientRepository.save(new Client("Petrov", "+375291234568"));
    }

    @Nested
    @DisplayName("Общая статистика")
    class OverviewTests {

        @Test
        @DisplayName("Корректная генерация статистики")
        void shouldReturnCorrectStats() {
            StatsResponse stats = statsService.getOverview();

            assertThat(stats.totalClients()).isEqualTo(2);
            assertThat(stats.clientsWithAccounts()).isEqualTo(1);
            assertThat(stats.clientsWithoutAccounts()).isEqualTo(1);
            assertThat(stats.totalAccounts()).isEqualTo(2);
            assertThat(stats.totalBalance()).isEqualByComparingTo(new BigDecimal("301.25"));
            assertThat(stats.averageBalance()).isEqualByComparingTo(new BigDecimal("150.63"));
            assertThat(stats.maxBalance()).isEqualByComparingTo(new BigDecimal("200.75"));
            assertThat(stats.minBalance()).isEqualByComparingTo(new BigDecimal("100.50"));
        }
    }
}