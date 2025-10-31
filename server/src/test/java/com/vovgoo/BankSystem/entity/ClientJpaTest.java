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
@DisplayName("Client JPA — поведение сущности и связей с Account")
class ClientJpaTest {

    @Autowired private ClientRepository clientRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private EntityManager em;

    private Client client;

    @BeforeEach
    void setup() {
        client = clientRepository.saveAndFlush(new Client("Ivanov", "+375291234567"));
    }

    @Nested
    @DisplayName("Операции с сохранением и связями")
    class PersistenceTests {

        @Test
        @DisplayName("Должен сохраняться с несколькими счетами (OneToMany связь)")
        void shouldSaveClientWithAccounts() {
            Account a1 = new Account(client);
            Account a2 = new Account(client);
            accountRepository.saveAllAndFlush(List.of(a1, a2));
            em.clear();

            Client saved = clientRepository.findById(client.getId()).orElseThrow();
            assertThat(saved.getAccounts())
                    .hasSize(2)
                    .allSatisfy(acc ->
                            assertThat(acc.getBalance()).isEqualByComparingTo(BigDecimal.ZERO));
        }

        @Test
        @DisplayName("Должен каскадно удалять связанные счета при удалении клиента")
        void shouldDeleteClientAndCascadeAccounts() {
            Account acc = accountRepository.saveAndFlush(new Account(client));
            em.clear();

            Client clientFromDb = clientRepository.findById(client.getId()).orElseThrow();
            clientRepository.delete(clientFromDb);
            clientRepository.flush();

            em.clear();
            assertThat(accountRepository.findById(acc.getId())).isEmpty();
        }

        @Test
        @DisplayName("Должен сохранять изменения фамилии и телефона")
        void shouldUpdateClientFields() {
            client.setLastName("Petrov");
            client.setPhone("+375441112233");
            clientRepository.saveAndFlush(client);

            em.clear();

            Client updated = clientRepository.findById(client.getId()).orElseThrow();
            assertThat(updated.getLastName()).isEqualTo("Petrov");
            assertThat(updated.getPhone()).isEqualTo("+375441112233");
        }

        @Test
        @DisplayName("Должен выбрасывать исключение при дублировании телефона")
        void shouldThrow_WhenPhoneDuplicate() {
            Client duplicate = new Client("Petrov", "+375291234567");
            assertThatThrownBy(() -> clientRepository.saveAndFlush(duplicate))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Работа коллекции счетов клиента")
    class AccountsCollectionTests {

        @Test
        @DisplayName("Список счетов должен быть неизменяемым извне")
        void accountsListShouldBeUnmodifiable() {
            assertThatThrownBy(() -> client.getAccounts().add(new Account(client)))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("Счета должны подгружаться лениво (Lazy Loading)")
        void shouldLazyLoadAccountsOnlyOnAccess() {
            accountRepository.saveAndFlush(new Account(client));
            em.clear();

            Client saved = clientRepository.findById(client.getId()).orElseThrow();
            var puUtil = em.getEntityManagerFactory().getPersistenceUnitUtil();

            assertThat(puUtil.isLoaded(saved, "accounts")).isFalse();
            assertThat(saved.getAccounts()).hasSize(1);
            assertThat(puUtil.isLoaded(saved, "accounts")).isTrue();
        }

        @Test
        @DisplayName("Метод findByIdWithAccounts должен загружать счета без N+1 проблемы")
        void shouldFetchClientsWithAccountsWithoutNPlusOne() {
            Client client2 = clientRepository.saveAndFlush(
                    new Client("Petrov", "+375441112233")
            );

            accountRepository.saveAllAndFlush(List.of(
                    new Account(client), new Account(client),
                    new Account(client2)
            ));

            em.clear();

            List<Client> clients = List.of(
                    clientRepository.findByIdWithAccounts(client.getId()).orElseThrow(),
                    clientRepository.findByIdWithAccounts(client2.getId()).orElseThrow()
            );

            clients.forEach(c -> assertThat(c.getAccounts()).isNotEmpty());
        }
    }
}