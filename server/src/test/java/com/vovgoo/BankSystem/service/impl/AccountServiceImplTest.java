package com.vovgoo.BankSystem.service.impl;

import com.vovgoo.BankSystem.config.properties.TransactionProperties;
import com.vovgoo.BankSystem.dto.account.request.*;
import com.vovgoo.BankSystem.entity.Account;
import com.vovgoo.BankSystem.entity.Client;
import com.vovgoo.BankSystem.exception.account.AccountBalanceNotZeroException;
import com.vovgoo.BankSystem.exception.account.InsufficientFundsException;
import com.vovgoo.BankSystem.exception.account.SameAccountTransferException;
import com.vovgoo.BankSystem.repository.AccountRepository;
import com.vovgoo.BankSystem.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("AccountServiceImpl — проверка операций со счетами")
class AccountServiceImplTest {

    @Autowired private AccountServiceImpl accountService;
    @Autowired private ClientRepository clientRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private TransactionProperties transactionProperties;

    private Client client;

    @BeforeEach
    void setUp() {
        client = clientRepository.saveAndFlush(new Client("Ivanov", "+375291234567"));
    }

    private Account reload(Account account) {
        accountRepository.flush();
        return accountRepository.findById(account.getId()).orElseThrow();
    }

    @Nested
    @DisplayName("Создание счета")
    class CreateTests {

        @Test
        @DisplayName("Успешное создание счета")
        void shouldCreateAccountSuccessfully() {
            CreateAccountRequest request = new CreateAccountRequest(client.getId());
            var response = accountService.create(request);

            assertThat(response.getMessage()).contains("успешно создан");
            Account account = accountRepository.findAll().get(0);
            assertThat(account.getClient()).isEqualTo(client);
            assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Попытка создания счета для несуществующего клиента")
        void shouldThrow_WhenClientNotFound() {
            CreateAccountRequest request = new CreateAccountRequest(UUID.randomUUID());
            assertThatThrownBy(() -> accountService.create(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Клиент не найден");
        }
    }

    @Nested
    @DisplayName("Удаление счета")
    class DeleteTests {

        @Test
        @DisplayName("Успешное удаление счета с нулевым балансом")
        void shouldDeleteAccountSuccessfully_WhenBalanceZero() {
            Account account = accountRepository.saveAndFlush(new Account(client));
            var response = accountService.delete(account.getId());

            assertThat(response.getMessage()).contains("успешно удалён");
            assertThat(accountRepository.findById(account.getId())).isEmpty();
        }

        @Test
        @DisplayName("Попытка удаления несуществующего счета")
        void shouldThrow_WhenAccountNotFound() {
            assertThatThrownBy(() -> accountService.delete(UUID.randomUUID()))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Счёт не найден");
        }

        @Test
        @DisplayName("Попытка удаления счета с ненулевым балансом")
        void shouldThrow_WhenBalanceNotZero() {
            Account account = new Account(client);
            account.deposit(BigDecimal.valueOf(50));
            accountRepository.saveAndFlush(account);

            assertThatThrownBy(() -> accountService.delete(account.getId()))
                    .isInstanceOf(AccountBalanceNotZeroException.class)
                    .hasMessageContaining("Невозможно удалить счёт");
        }
    }

    @Nested
    @DisplayName("Пополнение счета")
    class DepositTests {

        @Test
        @DisplayName("Успешное пополнение")
        void shouldDepositSuccessfully() {
            Account account = accountRepository.saveAndFlush(new Account(client));
            DepositAccountRequest request = new DepositAccountRequest(account.getId(), BigDecimal.valueOf(100));
            var response = accountService.deposit(request);

            assertThat(response.getMessage()).contains("успешно пополнен");
            assertThat(reload(account).getBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));
        }

        @Test
        @DisplayName("Попытка пополнить несуществующий счет")
        void shouldThrow_WhenAccountNotFound() {
            DepositAccountRequest request = new DepositAccountRequest(UUID.randomUUID(), BigDecimal.valueOf(50));
            assertThatThrownBy(() -> accountService.deposit(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Счёт не найден");
        }

        @Test
        @DisplayName("Попытка пополнить некорректной суммой")
        void shouldThrow_WhenAmountInvalid() {
            Account account = accountRepository.saveAndFlush(new Account(client));

            assertThatThrownBy(() -> accountService.deposit(new DepositAccountRequest(account.getId(), null)))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> accountService.deposit(new DepositAccountRequest(account.getId(), BigDecimal.ZERO)))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> accountService.deposit(new DepositAccountRequest(account.getId(), BigDecimal.valueOf(-1))))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> accountService.deposit(new DepositAccountRequest(account.getId(), new BigDecimal("10.123"))))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Снятие средств")
    class WithdrawTests {

        @Test
        @DisplayName("Успешное снятие")
        void shouldWithdrawSuccessfully() {
            Account account = new Account(client);
            account.deposit(BigDecimal.valueOf(100));
            accountRepository.saveAndFlush(account);

            WithdrawAccountRequest request = new WithdrawAccountRequest(account.getId(), BigDecimal.valueOf(40));
            var response = accountService.withdraw(request);

            assertThat(response.getMessage()).contains("успешно снято");
            assertThat(reload(account).getBalance()).isEqualByComparingTo(BigDecimal.valueOf(60));
        }

        @Test
        @DisplayName("Попытка снятия со несуществующего счета")
        void shouldThrow_WhenAccountNotFound() {
            WithdrawAccountRequest request = new WithdrawAccountRequest(UUID.randomUUID(), BigDecimal.valueOf(50));
            assertThatThrownBy(() -> accountService.withdraw(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Счёт не найден");
        }

        @Test
        @DisplayName("Попытка снять больше, чем баланс")
        void shouldThrow_WhenInsufficientFunds() {
            Account account = new Account(client);
            account.deposit(BigDecimal.valueOf(30));
            accountRepository.saveAndFlush(account);

            WithdrawAccountRequest request = new WithdrawAccountRequest(account.getId(), BigDecimal.valueOf(50));
            assertThatThrownBy(() -> accountService.withdraw(request))
                    .isInstanceOf(InsufficientFundsException.class);
        }
    }

    @Nested
    @DisplayName("Перевод между счетами")
    class TransferTests {

        @Test
        @DisplayName("Успешный перевод с комиссией")
        void shouldTransferSuccessfully() {
            Account from = new Account(client);
            from.deposit(BigDecimal.valueOf(200));
            accountRepository.saveAndFlush(from);

            Account to = new Account(client);
            accountRepository.saveAndFlush(to);

            BigDecimal amount = BigDecimal.valueOf(100);
            BigDecimal commission = amount.multiply(BigDecimal.valueOf(transactionProperties.getTransferCommissionPercent()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal total = amount.add(commission);

            TransferAccountRequest request = new TransferAccountRequest(from.getId(), to.getId(), amount);
            var response = accountService.transfer(request);

            assertThat(response.getMessage()).contains("Перевод");
            assertThat(reload(from).getBalance()).isEqualByComparingTo(BigDecimal.valueOf(200).subtract(total));
            assertThat(reload(to).getBalance()).isEqualByComparingTo(amount);
        }

        @Test
        @DisplayName("Попытка перевода на тот же счет")
        void shouldThrow_WhenSameAccount() {
            TransferAccountRequest request = new TransferAccountRequest(client.getId(), client.getId(), BigDecimal.valueOf(50));
            assertThatThrownBy(() -> accountService.transfer(request))
                    .isInstanceOf(SameAccountTransferException.class);
        }

        @Test
        @DisplayName("Попытка перевода с несуществующего счета отправителя")
        void shouldThrow_WhenSenderNotFound() {
            Account to = accountRepository.saveAndFlush(new Account(client));
            TransferAccountRequest request = new TransferAccountRequest(UUID.randomUUID(), to.getId(), BigDecimal.valueOf(50));

            assertThatThrownBy(() -> accountService.transfer(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Один из счетов не найден");
        }

        @Test
        @DisplayName("Попытка перевода на несуществующий счет получателя")
        void shouldThrow_WhenReceiverNotFound() {
            Account from = accountRepository.saveAndFlush(new Account(client));
            TransferAccountRequest request = new TransferAccountRequest(from.getId(), UUID.randomUUID(), BigDecimal.valueOf(50));

            assertThatThrownBy(() -> accountService.transfer(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Один из счетов не найден");
        }

        @Test
        @DisplayName("Попытка перевода суммы больше баланса")
        void shouldThrow_WhenInsufficientFunds() {
            Account from = new Account(client);
            from.deposit(BigDecimal.valueOf(50));
            accountRepository.saveAndFlush(from);
            Account to = accountRepository.saveAndFlush(new Account(client));

            TransferAccountRequest request = new TransferAccountRequest(from.getId(), to.getId(), BigDecimal.valueOf(100));
            assertThatThrownBy(() -> accountService.transfer(request))
                    .isInstanceOf(InsufficientFundsException.class);
        }
    }
}