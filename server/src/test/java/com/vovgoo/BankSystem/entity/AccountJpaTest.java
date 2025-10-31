package com.vovgoo.BankSystem.entity;

import com.vovgoo.BankSystem.repository.AccountRepository;
import com.vovgoo.BankSystem.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class AccountJpaTest {

    @Autowired private ClientRepository clientRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private EntityManager em;

    private Client client;

    @BeforeEach
    void setup() {
        client = new Client("Ivanov", "+375291234567");
        clientRepository.saveAndFlush(client);
    }

    @Test
    void shouldSaveAndLoadAccount() {
        Account acc = new Account(client);
        acc.deposit(BigDecimal.valueOf(100));
        accountRepository.saveAndFlush(acc);

        em.clear();

        Account fromDb = accountRepository.findById(acc.getId()).orElseThrow();
        assertThat(fromDb.getBalance()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(fromDb.getClient().getId()).isEqualTo(client.getId());
    }

    @Test
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
    void shouldThrow_WhenWithdrawExceedsBalance() {
        Account acc = new Account(client);
        acc.deposit(BigDecimal.valueOf(30));
        accountRepository.saveAndFlush(acc);

        assertThatThrownBy(() -> acc.withdraw(BigDecimal.valueOf(100)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Недостаточно средств");
    }

    @Test
    void shouldDeleteAccountWithoutDeletingClient() {
        Account acc = accountRepository.saveAndFlush(new Account(client));
        accountRepository.delete(acc);
        accountRepository.flush();

        assertThat(accountRepository.findById(acc.getId())).isEmpty();
        assertThat(clientRepository.findById(client.getId())).isPresent();
    }

    @Test
    void shouldLazyLoadClient() {
        Account acc = accountRepository.saveAndFlush(new Account(client));
        em.clear();

        Account fromDb = accountRepository.findById(acc.getId()).orElseThrow();
        var puUtil = em.getEntityManagerFactory().getPersistenceUnitUtil();

        assertThat(puUtil.isLoaded(fromDb, "client")).isFalse();
        fromDb.getClient().getLastName();
        assertThat(puUtil.isLoaded(fromDb, "client")).isTrue();
    }

    @Test
    void shouldPersistMultipleAccountsForClient() {
        Account a1 = new Account(client);
        Account a2 = new Account(client);
        a1.deposit(BigDecimal.TEN);
        a2.deposit(BigDecimal.valueOf(20));

        accountRepository.saveAllAndFlush(List.of(a1, a2));
        em.clear();

        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).hasSize(2)
                .extracting(Account::getBalance)
                .containsExactlyInAnyOrder(new BigDecimal("10.00"), new BigDecimal("20.00"));
    }

    @Test
    void shouldThrow_WhenDepositOrWithdrawWithMoreThanTwoDecimals() {
        Account acc = new Account(client);
        accountRepository.saveAndFlush(acc);

        assertThatThrownBy(() -> acc.deposit(new BigDecimal("10.123")))
                .isInstanceOf(IllegalArgumentException.class);

        acc.deposit(BigDecimal.valueOf(50));
        assertThatThrownBy(() -> acc.withdraw(new BigDecimal("10.123")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}