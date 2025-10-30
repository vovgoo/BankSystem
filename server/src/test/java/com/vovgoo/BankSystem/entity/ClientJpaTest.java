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
class ClientJpaTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveClientWithAccounts_CascadePersist() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.save(client);
        clientRepository.flush();

        Account acc1 = new Account(client);
        Account acc2 = new Account(client);
        accountRepository.saveAll(List.of(acc1, acc2));
        accountRepository.flush();

        entityManager.clear();

        Client saved = clientRepository.findById(client.getId()).orElseThrow();

        assertThat(saved.getAccounts()).hasSize(2);
        assertThat(saved.getAccounts())
                .extracting(Account::getBalance)
                .allSatisfy(balance -> assertThat(balance.compareTo(BigDecimal.ZERO)).isZero());
    }

    @Test
    void shouldDeleteClient_RemoveAccountsOrphanRemoval() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.save(client);
        clientRepository.flush();

        Account acc = new Account(client);
        accountRepository.save(acc);
        accountRepository.flush();

        entityManager.clear();

        Client clientFromDb = clientRepository.findById(client.getId()).orElseThrow();
        clientRepository.delete(clientFromDb);
        clientRepository.flush();

        entityManager.clear();

        assertThat(accountRepository.findById(acc.getId())).isEmpty();
    }

    @Test
    void shouldUpdateClientFields() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.save(client);

        Client saved = clientRepository.findById(client.getId()).orElseThrow();
        saved.setLastName("Petrov");
        saved.setPhone("+375441112233");
        clientRepository.save(saved);

        Client updated = clientRepository.findById(client.getId()).orElseThrow();
        assertThat(updated.getLastName()).isEqualTo("Petrov");
        assertThat(updated.getPhone()).isEqualTo("+375441112233");
    }

    @Test
    void shouldNotSaveClientWithDuplicatePhone() {
        Client client1 = new Client("Ivanov", "+375291234567");
        clientRepository.save(client1);

        Client client2 = new Client("Petrov", "+375291234567");
        assertThatThrownBy(() -> clientRepository.saveAndFlush(client2))
                .isInstanceOf(Exception.class);
    }

    @Test
    void shouldDepositAndWithdrawCorrectly() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.save(client);
        clientRepository.flush();

        Account account = new Account(client);
        accountRepository.save(account);
        accountRepository.flush();

        entityManager.clear();

        Account accFromDb = accountRepository.findById(account.getId()).orElseThrow();
        accFromDb.deposit(BigDecimal.valueOf(100));
        accountRepository.flush();
        entityManager.clear();

        accFromDb = accountRepository.findById(account.getId()).orElseThrow();
        assertThat(accFromDb.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));

        accFromDb.withdraw(BigDecimal.valueOf(40));
        accountRepository.flush();
        entityManager.clear();

        accFromDb = accountRepository.findById(account.getId()).orElseThrow();
        assertThat(accFromDb.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(60));
    }

    @Test
    void depositShouldThrow_WhenAmountInvalid() {
        Client client = new Client("Ivanov", "+375291234567");
        Account account = new Account(client);

        assertThatThrownBy(() -> account.deposit(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> account.deposit(BigDecimal.valueOf(-10)))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> account.deposit(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withdrawShouldThrow_WhenAmountInvalidOrExceedsBalance() {
        Client client = new Client("Ivanov", "+375291234567");
        Account account = new Account(client);
        account.deposit(BigDecimal.valueOf(50));

        assertThatThrownBy(() -> account.withdraw(BigDecimal.valueOf(100)))
                .isInstanceOf(IllegalStateException.class);

        assertThatThrownBy(() -> account.withdraw(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> account.withdraw(BigDecimal.valueOf(-5)))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> account.withdraw(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void accountsListShouldBeUnmodifiable() {
        Client client = new Client("Ivanov", "+375291234567");
        assertThatThrownBy(() -> client.getAccounts().add(new Account(client)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void lazyLoadingAccounts_ShouldNotInitializeUntilAccess() {
        Client client = new Client("Ivanov", "+375291234567");
        clientRepository.saveAndFlush(client);

        Account acc = new Account(client);
        accountRepository.saveAndFlush(acc);

        entityManager.clear();

        Client saved = clientRepository.findById(client.getId()).orElseThrow();

        boolean loaded = entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .isLoaded(saved, "accounts");
        assertThat(loaded).isFalse();

        int size = saved.getAccounts().size();
        assertThat(size).isEqualTo(1);

        loaded = entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .isLoaded(saved, "accounts");
        assertThat(loaded).isTrue();
    }

    @Test
    void nPlusOneTestForClientsAndAccounts() {
        Client client1 = new Client("Ivanov", "+375291234567");
        Client client2 = new Client("Petrov", "+375441112233");
        clientRepository.saveAll(List.of(client1, client2));
        clientRepository.flush();

        Account a1 = new Account(client1);
        Account a2 = new Account(client1);
        Account a3 = new Account(client2);
        accountRepository.saveAll(List.of(a1, a2, a3));
        accountRepository.flush();

        entityManager.clear();

        Client client1WithAccounts = clientRepository.findByIdWithAccounts(client1.getId()).orElseThrow();
        Client client2WithAccounts = clientRepository.findByIdWithAccounts(client2.getId()).orElseThrow();

        List<Client> clients = List.of(client1WithAccounts, client2WithAccounts);

        clients.forEach(c -> {
            assertThat(c.getAccounts()).isNotEmpty();
        });
    }
}