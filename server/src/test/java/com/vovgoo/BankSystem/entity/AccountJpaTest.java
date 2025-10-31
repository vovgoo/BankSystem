package com.vovgoo.BankSystem.entity;

import com.vovgoo.BankSystem.repository.AccountRepository;
import com.vovgoo.BankSystem.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Account JPA — поведение сущности и репозитория")
class AccountJpaTest {

    @Autowired private ClientRepository clientRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private EntityManager em;

    private Client client;

    @BeforeEach
    void setup() {
        client = clientRepository.saveAndFlush(new Client("Ivanov", "+375291234567"));
    }

    @Nested
    @DisplayName("Базовые операции сохранения и загрузки")
    class PersistenceTests {

        @Test
        @DisplayName("Должен сохраняться и загружаться с корректными данными")
        void shouldSaveAndLoadAccount() {
            Account acc = new Account(client);
            acc.deposit(BigDecimal.valueOf(100));
            accountRepository.saveAndFlush(acc);
            em.clear();

            Account fromDb = accountRepository.findById(acc.getId()).orElseThrow();
            assertThat(fromDb.getBalance()).isEqualByComparingTo("100.00");
            assertThat(fromDb.getClient().getId()).isEqualTo(client.getId());
        }

        @Test
        @DisplayName("Должен сохранять несколько счетов для одного клиента")
        void shouldPersistMultipleAccountsForClient() {
            Account a1 = new Account(client);
            Account a2 = new Account(client);
            a1.deposit(BigDecimal.TEN);
            a2.deposit(BigDecimal.valueOf(20));

            accountRepository.saveAllAndFlush(List.of(a1, a2));
            em.clear();

            List<Account> accounts = accountRepository.findAll();
            assertThat(accounts)
                    .hasSize(2)
                    .extracting(Account::getBalance)
                    .containsExactlyInAnyOrder(new BigDecimal("10.00"), new BigDecimal("20.00"));
        }

        @Test
        @DisplayName("Удаление счёта не должно удалять клиента")
        void shouldDeleteAccountWithoutDeletingClient() {
            Account acc = accountRepository.saveAndFlush(new Account(client));
            accountRepository.delete(acc);
            accountRepository.flush();

            assertThat(accountRepository.findById(acc.getId())).isEmpty();
            assertThat(clientRepository.findById(client.getId())).isPresent();
        }

        @Test
        @DisplayName("Клиент должен быть загружаемым лениво (Lazy Loading)")
        void shouldLazyLoadClient() {
            Account acc = accountRepository.saveAndFlush(new Account(client));
            em.clear();

            Account fromDb = accountRepository.findById(acc.getId()).orElseThrow();
            var puUtil = em.getEntityManagerFactory().getPersistenceUnitUtil();

            assertThat(puUtil.isLoaded(fromDb, "client")).isFalse();
            fromDb.getClient().getLastName(); // триггер lazy
            assertThat(puUtil.isLoaded(fromDb, "client")).isTrue();
        }
    }

    @Nested
    @DisplayName("Операции с балансом")
    class BalanceOperationsTests {

        @Test
        @DisplayName("Должен корректно обновлять баланс при депозите")
        void shouldUpdateBalance() {
            Account acc = new Account(client);
            acc.deposit(BigDecimal.valueOf(50));
            accountRepository.saveAndFlush(acc);

            acc.deposit(BigDecimal.valueOf(25.75));
            accountRepository.flush();

            em.clear();
            Account updated = accountRepository.findById(acc.getId()).orElseThrow();
            assertThat(updated.getBalance()).isEqualByComparingTo("75.75");
        }

        @Test
        @DisplayName("Должен корректно уменьшать баланс при снятии средств")
        void shouldWithdrawCorrectly() {
            Account acc = new Account(client);
            acc.deposit(BigDecimal.valueOf(200));
            accountRepository.saveAndFlush(acc);

            acc.withdraw(BigDecimal.valueOf(50.50));
            accountRepository.flush();

            em.clear();
            Account updated = accountRepository.findById(acc.getId()).orElseThrow();
            assertThat(updated.getBalance()).isEqualByComparingTo("149.50");
        }

        @Test
        @DisplayName("Должен выбрасывать исключение, если сумма снятия превышает баланс")
        void shouldThrow_WhenWithdrawExceedsBalance() {
            Account acc = new Account(client);
            acc.deposit(BigDecimal.valueOf(30));
            accountRepository.saveAndFlush(acc);

            assertThatThrownBy(() -> acc.withdraw(BigDecimal.valueOf(100)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Недостаточно средств");
        }

        @Test
        @DisplayName("Должен выбрасывать исключение при суммах с более чем двумя знаками после запятой")
        void shouldThrow_WhenDepositOrWithdrawWithMoreThanTwoDecimals() {
            Account acc = accountRepository.saveAndFlush(new Account(client));

            assertThatThrownBy(() -> acc.deposit(new BigDecimal("10.123")))
                    .isInstanceOf(IllegalArgumentException.class);

            acc.deposit(BigDecimal.valueOf(50));
            assertThatThrownBy(() -> acc.withdraw(new BigDecimal("10.123")))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}