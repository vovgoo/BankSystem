package com.vovgoo.BankSystem.mapper;

import com.vovgoo.BankSystem.dto.account.response.AccountSummaryResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientDetailsResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.entity.Account;
import com.vovgoo.BankSystem.entity.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    @Nested
    @DisplayName("toClientDetailsResponse")
    class ToClientDetailsResponseTests {

        @Test
        @DisplayName("Возвращает null, если клиент null")
        void shouldReturnNull_whenClientIsNull() {
            ClientDetailsResponse result = mapper.toClientDetailsResponse(null, Page.empty());
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Возвращает пустой список счетов, если accounts null")
        void shouldReturnEmptyAccounts_whenAccountsIsNull() {
            Client client = new Client("Ivanov", "+375291234567");
            ClientDetailsResponse result = mapper.toClientDetailsResponse(client, null);

            assertThat(result).isNotNull();
            assertThat(result.accounts()).isNotNull();
            assertThat(result.accounts().getContent()).isEmpty();
        }

        @Test
        @DisplayName("Возвращает пустой список счетов, если accounts пустая страница")
        void shouldReturnEmptyAccounts_whenAccountsIsEmpty() {
            Client client = new Client("Ivanov", "+375291234567");
            ClientDetailsResponse result = mapper.toClientDetailsResponse(client, Page.empty());

            assertThat(result.accounts()).isNotNull();
            assertThat(result.accounts().getContent()).isEmpty();
        }

        @Test
        @DisplayName("Корректно отображает аккаунты с балансами и id")
        void shouldMapAccountsCorrectly() {
            Client client = new Client("Ivanov", "+375291234567");

            Account a1 = new Account(client); a1.deposit(BigDecimal.valueOf(100));
            Account a2 = new Account(client); a2.deposit(BigDecimal.valueOf(200));
            Page<Account> accountsPage = new PageImpl<>(List.of(a1, a2));

            ClientDetailsResponse result = mapper.toClientDetailsResponse(client, accountsPage);

            assertThat(result.accounts()).isNotNull();
            assertThat(result.accounts().getContent()).hasSize(2);

            List<BigDecimal> balances = result.accounts().getContent().stream()
                    .map(AccountSummaryResponse::balance)
                    .toList();
            assertThat(balances)
                    .usingElementComparator(BigDecimal::compareTo)
                    .containsExactlyInAnyOrder(BigDecimal.valueOf(100), BigDecimal.valueOf(200));

            List<UUID> ids = result.accounts().getContent().stream()
                    .map(AccountSummaryResponse::id)
                    .toList();
            assertThat(ids).containsExactlyInAnyOrder(a1.getId(), a2.getId());
        }

        @Test
        @DisplayName("Корректно обрабатывает несколько аккаунтов с одинаковым балансом")
        void shouldHandleMultipleAccounts_withSameBalance() {
            Client client = new Client("Sidorov", "+375291112233");

            Account a1 = new Account(client); a1.deposit(BigDecimal.valueOf(100));
            Account a2 = new Account(client); a2.deposit(BigDecimal.valueOf(100));
            Page<Account> accountsPage = new PageImpl<>(List.of(a1, a2));

            ClientDetailsResponse result = mapper.toClientDetailsResponse(client, accountsPage);

            assertThat(result.accounts()).isNotNull();
            assertThat(result.accounts().getContent()).hasSize(2);
            result.accounts().getContent()
                    .forEach(acc -> assertThat(acc.balance()).isEqualByComparingTo(BigDecimal.valueOf(100)));
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