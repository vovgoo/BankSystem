package com.vovgoo.BankSystem.entity;

import com.vovgoo.BankSystem.repository.AccountRepository;
import com.vovgoo.BankSystem.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class AccountJpaTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndLoadAccount() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.saveAndFlush(client);

        Account account = new Account(client);
        account.deposit(BigDecimal.valueOf(100));
        accountRepository.saveAndFlush(account);

        entityManager.clear();

        Account fromDb = accountRepository.findById(account.getId()).orElseThrow();
        assertThat(fromDb.getBalance()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(fromDb.getBalance().scale()).isEqualTo(2);
        assertThat(fromDb.getClient().getId()).isEqualTo(client.getId());
    }

    @Test
    void shouldUpdateBalanceInDb() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.saveAndFlush(client);

        Account account = new Account(client);
        account.deposit(BigDecimal.valueOf(50));
        accountRepository.saveAndFlush(account);

        entityManager.clear();

        Account fromDb = accountRepository.findById(account.getId()).orElseThrow();
        fromDb.deposit(BigDecimal.valueOf(25.75));
        accountRepository.flush();
        entityManager.clear();

        Account updated = accountRepository.findById(account.getId()).orElseThrow();
        assertThat(updated.getBalance()).isEqualByComparingTo(new BigDecimal("75.75"));
        assertThat(updated.getBalance().scale()).isEqualTo(2);
    }

    @Test
    void withdraw_shouldPersistCorrectly() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.saveAndFlush(client);

        Account account = new Account(client);
        account.deposit(BigDecimal.valueOf(200));
        accountRepository.saveAndFlush(account);

        entityManager.clear();

        Account fromDb = accountRepository.findById(account.getId()).orElseThrow();
        fromDb.withdraw(BigDecimal.valueOf(50.50));
        accountRepository.flush();
        entityManager.clear();

        Account updated = accountRepository.findById(account.getId()).orElseThrow();
        assertThat(updated.getBalance()).isEqualByComparingTo(new BigDecimal("149.50"));
        assertThat(updated.getBalance().scale()).isEqualTo(2);
    }

    @Test
    void shouldThrowWhenWithdrawExceedsBalance() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.saveAndFlush(client);

        Account account = new Account(client);
        account.deposit(BigDecimal.valueOf(30));
        accountRepository.saveAndFlush(account);

        entityManager.clear();

        Account fromDb = accountRepository.findById(account.getId()).orElseThrow();
        assertThatThrownBy(() -> fromDb.withdraw(BigDecimal.valueOf(100)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Недостаточно средств");
    }

    @Test
    void shouldDeleteAccount_withoutAffectingClient() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.saveAndFlush(client);

        Account account = new Account(client);
        accountRepository.saveAndFlush(account);

        accountRepository.delete(account);
        accountRepository.flush();

        assertThat(accountRepository.findById(account.getId())).isEmpty();
        assertThat(clientRepository.findById(client.getId())).isPresent();
    }

    @Test
    void lazyLoadingClient_shouldNotLoadUntilAccess() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.saveAndFlush(client);

        Account account = new Account(client);
        accountRepository.saveAndFlush(account);

        entityManager.clear();

        Account fromDb = accountRepository.findById(account.getId()).orElseThrow();
        boolean loaded = entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .isLoaded(fromDb, "client");
        assertThat(loaded).isFalse();

        fromDb.getClient().getLastName();

        loaded = entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .isLoaded(fromDb, "client");
        assertThat(loaded).isTrue();
    }

    @Test
    void multipleAccounts_forSameClient_shouldBePersistedAndFetchedCorrectly() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.saveAndFlush(client);

        Account acc1 = new Account(client);
        Account acc2 = new Account(client);
        acc1.deposit(BigDecimal.valueOf(10));
        acc2.deposit(BigDecimal.valueOf(20));

        accountRepository.saveAll(List.of(acc1, acc2));
        accountRepository.flush();
        entityManager.clear();

        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).hasSize(2);
        assertThat(accounts).extracting(Account::getBalance)
                .containsExactlyInAnyOrder(new BigDecimal("10.00"), new BigDecimal("20.00"));
    }

    @Test
    void deposit_shouldThrow_WhenMoreThanTwoDecimalPlaces() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.saveAndFlush(client);

        Account account = new Account(client);
        accountRepository.saveAndFlush(account);

        assertThatThrownBy(() -> account.deposit(new BigDecimal("10.123")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withdraw_shouldThrow_WhenMoreThanTwoDecimalPlaces() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.saveAndFlush(client);

        Account account = new Account(client);
        account.deposit(BigDecimal.valueOf(100));
        accountRepository.saveAndFlush(account);

        entityManager.clear();

        Account fromDb = accountRepository.findById(account.getId()).orElseThrow();
        assertThatThrownBy(() -> fromDb.withdraw(new BigDecimal("10.123")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}