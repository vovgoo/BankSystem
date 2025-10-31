package com.vovgoo.BankSystem.controller;

import com.vovgoo.BankSystem.entity.Account;
import com.vovgoo.BankSystem.entity.Client;
import com.vovgoo.BankSystem.repository.AccountRepository;
import com.vovgoo.BankSystem.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("StatsController — тесты статистики системы")
class StatsControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ClientRepository clientRepository;
    @Autowired private AccountRepository accountRepository;

    @BeforeEach
    void cleanDb() {
        accountRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Nested
    @DisplayName("Обзор статистики")
    class OverviewStatsTests {

        @BeforeEach
        void setupData() {
            Client clientWithAccounts = clientRepository.save(new Client("Ivanov", "+375291234567"));
            Account acc1 = new Account(clientWithAccounts);
            acc1.deposit(new BigDecimal("100.50"));
            Account acc2 = new Account(clientWithAccounts);
            acc2.deposit(new BigDecimal("200.75"));
            accountRepository.save(acc1);
            accountRepository.save(acc2);

            clientRepository.save(new Client("Petrov", "+375291234568"));
        }

        @Test
        @DisplayName("Должен вернуть корректную статистику по клиентам и счетам")
        void shouldReturnCorrectStats() throws Exception {
            mockMvc.perform(get("/api/v1/stats")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalClients").value(2))
                    .andExpect(jsonPath("$.clientsWithAccounts").value(1))
                    .andExpect(jsonPath("$.clientsWithoutAccounts").value(1))
                    .andExpect(jsonPath("$.totalAccounts").value(2))
                    .andExpect(jsonPath("$.totalBalance").value(301.25))
                    .andExpect(jsonPath("$.averageBalance").value(150.63))
                    .andExpect(jsonPath("$.maxBalance").value(200.75))
                    .andExpect(jsonPath("$.minBalance").value(100.50));
        }
    }

    @Nested
    @DisplayName("Обзор статистики при пустой базе")
    class EmptyStatsTests {

        @Test
        @DisplayName("Должен вернуть нули, если клиентов и счетов нет")
        void shouldReturnZerosWhenNoData() throws Exception {
            mockMvc.perform(get("/api/v1/stats")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalClients").value(0))
                    .andExpect(jsonPath("$.clientsWithAccounts").value(0))
                    .andExpect(jsonPath("$.clientsWithoutAccounts").value(0))
                    .andExpect(jsonPath("$.totalAccounts").value(0))
                    .andExpect(jsonPath("$.totalBalance").value(0.0))
                    .andExpect(jsonPath("$.averageBalance").value(0.0))
                    .andExpect(jsonPath("$.maxBalance").value(0.0))
                    .andExpect(jsonPath("$.minBalance").value(0.0));
        }
    }
}