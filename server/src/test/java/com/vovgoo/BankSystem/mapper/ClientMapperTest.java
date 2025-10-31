package com.vovgoo.BankSystem.mapper;

import com.vovgoo.BankSystem.dto.account.response.AccountSummaryResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientDetailsResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.entity.Account;
import com.vovgoo.BankSystem.entity.Client;
import com.vovgoo.BankSystem.repository.AccountRepository;
import com.vovgoo.BankSystem.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("ClientMapper — проверка преобразований Client ↔ DTO")
class ClientMapperTest {

    @Autowired private ClientMapper mapper;
    @Autowired private ClientRepository clientRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private EntityManager entityManager;

    @Nested
    @DisplayName("toClientDetailsResponse")
    class ToClientDetailsResponseTests {

        @Test
        @DisplayName("Возвращает null, если клиент null")
        void shouldReturnNull_whenClientIsNull() {
            ClientDetailsResponse result = mapper.toClientDetailsResponse(null);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Возвращает пустой список счетов, если у клиента нет аккаунтов")
        void shouldReturnEmptyAccounts_whenClientHasNoAccounts() {
            Client client = new Client("Ivanov", "+375291234567");
            clientRepository.saveAndFlush(client);
            entityManager.clear();

            Client clientFromDb = clientRepository.findById(client.getId()).orElseThrow();
            ClientDetailsResponse result = mapper.toClientDetailsResponse(clientFromDb);

            assertThat(result.accounts()).isEmpty();
        }

        @Test
        @DisplayName("Корректно отображает аккаунты с балансами и id")
        void shouldMapAccountsCorrectly() {
            Client client = new Client("Ivanov", "+375291234567");
            clientRepository.saveAndFlush(client);

            Account a1 = new Account(client); a1.deposit(BigDecimal.valueOf(100));
            Account a2 = new Account(client); a2.deposit(BigDecimal.valueOf(200));
            accountRepository.saveAll(List.of(a1, a2));
            accountRepository.flush();
            entityManager.clear();

            Client clientFromDb = clientRepository.findById(client.getId()).orElseThrow();
            ClientDetailsResponse result = mapper.toClientDetailsResponse(clientFromDb);

            assertThat(result.accounts()).hasSize(2);
            List<BigDecimal> balances = result.accounts().stream()
                    .map(AccountSummaryResponse::balance).toList();
            assertThat(balances)
                    .usingElementComparator(BigDecimal::compareTo)
                    .containsExactlyInAnyOrder(BigDecimal.valueOf(100), BigDecimal.valueOf(200));

            List<UUID> ids = result.accounts().stream()
                    .map(AccountSummaryResponse::id).toList();
            assertThat(ids).containsExactlyInAnyOrder(a1.getId(), a2.getId());
        }

        @Test
        @DisplayName("Корректно обрабатывает несколько аккаунтов с одинаковым балансом")
        void shouldHandleMultipleAccounts_withSameBalance() {
            Client client = new Client("Sidorov", "+375291112233");
            clientRepository.saveAndFlush(client);

            Account a1 = new Account(client); a1.deposit(BigDecimal.valueOf(100));
            Account a2 = new Account(client); a2.deposit(BigDecimal.valueOf(100));
            accountRepository.saveAll(List.of(a1, a2));
            accountRepository.flush();
            entityManager.clear();

            Client clientFromDb = clientRepository.findById(client.getId()).orElseThrow();
            ClientDetailsResponse result = mapper.toClientDetailsResponse(clientFromDb);

            assertThat(result.accounts()).hasSize(2);
            result.accounts().forEach(acc -> assertThat(acc.balance()).isEqualByComparingTo(BigDecimal.valueOf(100)));
        }

        @Test
        @DisplayName("Возвращает пустой список, если getAccounts() возвращает null")
        void shouldReturnEmptyList_whenAccountsCollectionIsNull() {
            Client client = new Client("Nullov", "+375298765432") {
                @Override
                public List<Account> getAccounts() { return null; }
            };
            ClientDetailsResponse result = mapper.toClientDetailsResponse(client);
            assertThat(result.accounts()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toClientResponse")
    class ToClientResponseTests {

        @Test
        @DisplayName("Возвращает null, если клиент null")
        void shouldReturnNull_whenClientIsNull() {
            ClientResponse result = mapper.toClientResponse(null, BigDecimal.valueOf(100));
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Корректно отображает поля клиента и totalBalance")
        void shouldMapFieldsCorrectly() {
            Client client = new Client("Petrov", "+375441112233");
            BigDecimal totalBalance = BigDecimal.valueOf(500.50);

            ClientResponse result = mapper.toClientResponse(client, totalBalance);

            assertThat(result.id()).isEqualTo(client.getId());
            assertThat(result.lastName()).isEqualTo(client.getLastName());
            assertThat(result.phone()).isEqualTo(client.getPhone());
            assertThat(result.totalBalance()).isEqualByComparingTo(totalBalance);
        }
    }
}