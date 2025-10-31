package com.vovgoo.BankSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vovgoo.BankSystem.dto.account.request.*;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("AccountController — операции со счетами")
class AccountControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ClientRepository clientRepository;
    @Autowired private AccountRepository accountRepository;

    private Client client;
    private Account account;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
        clientRepository.deleteAll();
        client = clientRepository.save(new Client("Smith", "+375291234567"));
        account = accountRepository.save(new Account(client));
    }

    @Nested
    @DisplayName("Создание счёта")
    class CreateAccountTests {

        @Test
        @DisplayName("Успешное создание счёта для существующего клиента")
        void createAccount_Success() throws Exception {
            CreateAccountRequest request = new CreateAccountRequest(client.getId());

            mockMvc.perform(post("/api/v1/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("APPROVED"));
        }

        @Test
        @DisplayName("Попытка создать счёт для несуществующего клиента")
        void createAccount_ClientNotFound() throws Exception {
            CreateAccountRequest request = new CreateAccountRequest(UUID.randomUUID());

            mockMvc.perform(post("/api/v1/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }

        @Test
        @DisplayName("Попытка создать счёт с пустым или некорректным запросом")
        void createAccount_InvalidRequest() throws Exception {
            mockMvc.perform(post("/api/v1/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }
    }

    @Nested
    @DisplayName("Удаление счёта")
    class DeleteAccountTests {

        @Test
        @DisplayName("Успешное удаление пустого счёта")
        void deleteAccount_Success() throws Exception {
            mockMvc.perform(delete("/api/v1/accounts/{id}", account.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("APPROVED"));
        }

        @Test
        @DisplayName("Попытка удалить несуществующий счёт")
        void deleteAccount_NotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/accounts/{id}", UUID.randomUUID()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }

        @Test
        @DisplayName("Попытка удалить счёт с ненулевым балансом")
        void deleteAccount_BalanceNotZero() throws Exception {
            account.deposit(BigDecimal.valueOf(100));
            accountRepository.save(account);

            mockMvc.perform(delete("/api/v1/accounts/{id}", account.getId()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }
    }

    @Nested
    @DisplayName("Пополнение счёта")
    class DepositTests {

        @Test
        @DisplayName("Успешное пополнение счёта")
        void deposit_Success() throws Exception {
            DepositAccountRequest request = new DepositAccountRequest(account.getId(), BigDecimal.valueOf(50));

            mockMvc.perform(post("/api/v1/accounts/deposit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("APPROVED"));
        }

        @Test
        @DisplayName("Попытка пополнить несуществующий счёт")
        void deposit_AccountNotFound() throws Exception {
            DepositAccountRequest request = new DepositAccountRequest(UUID.randomUUID(), BigDecimal.valueOf(50));

            mockMvc.perform(post("/api/v1/accounts/deposit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }

        @Test
        @DisplayName("Попытка пополнить счёт отрицательной суммой")
        void deposit_InvalidAmount() throws Exception {
            DepositAccountRequest request = new DepositAccountRequest(account.getId(), BigDecimal.valueOf(-10));

            mockMvc.perform(post("/api/v1/accounts/deposit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }
    }

    @Nested
    @DisplayName("Снятие со счёта")
    class WithdrawTests {

        @Test
        @DisplayName("Успешное снятие средств со счёта")
        void withdraw_Success() throws Exception {
            account.deposit(BigDecimal.valueOf(100));
            accountRepository.save(account);

            WithdrawAccountRequest request = new WithdrawAccountRequest(account.getId(), BigDecimal.valueOf(50));

            mockMvc.perform(post("/api/v1/accounts/withdraw")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("APPROVED"));
        }

        @Test
        @DisplayName("Попытка снять больше, чем баланс счёта")
        void withdraw_InsufficientFunds() throws Exception {
            WithdrawAccountRequest request = new WithdrawAccountRequest(account.getId(), BigDecimal.valueOf(50));

            mockMvc.perform(post("/api/v1/accounts/withdraw")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }

        @Test
        @DisplayName("Попытка снять со счёта, которого не существует")
        void withdraw_AccountNotFound() throws Exception {
            WithdrawAccountRequest request = new WithdrawAccountRequest(UUID.randomUUID(), BigDecimal.valueOf(50));

            mockMvc.perform(post("/api/v1/accounts/withdraw")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }
    }

    @Nested
    @DisplayName("Перевод между счетами")
    class TransferTests {

        private Account toAccount;

        @BeforeEach
        void setupAccounts() {
            toAccount = accountRepository.save(new Account(client));
            account.deposit(BigDecimal.valueOf(200));
            accountRepository.save(account);
        }

        @Test
        @DisplayName("Успешный перевод между счетами")
        void transfer_Success() throws Exception {
            TransferAccountRequest request = new TransferAccountRequest(account.getId(), toAccount.getId(), BigDecimal.valueOf(100));

            mockMvc.perform(post("/api/v1/accounts/transfer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("APPROVED"));
        }

        @Test
        @DisplayName("Попытка перевести средства на тот же счёт")
        void transfer_SameAccount() throws Exception {
            TransferAccountRequest request = new TransferAccountRequest(account.getId(), account.getId(), BigDecimal.valueOf(50));

            mockMvc.perform(post("/api/v1/accounts/transfer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }

        @Test
        @DisplayName("Попытка перевести больше средств, чем есть на счёте")
        void transfer_InsufficientFunds() throws Exception {
            TransferAccountRequest request = new TransferAccountRequest(account.getId(), toAccount.getId(), BigDecimal.valueOf(500));

            mockMvc.perform(post("/api/v1/accounts/transfer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }

        @Test
        @DisplayName("Попытка перевести средства со счёта, которого не существует")
        void transfer_AccountNotFound() throws Exception {
            TransferAccountRequest request = new TransferAccountRequest(UUID.randomUUID(), toAccount.getId(), BigDecimal.valueOf(50));

            mockMvc.perform(post("/api/v1/accounts/transfer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }
    }
}