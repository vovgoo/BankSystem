package com.vovgoo.BankSystem.service.impl;

import com.vovgoo.BankSystem.dto.client.request.CreateClientRequest;
import com.vovgoo.BankSystem.dto.client.request.SearchClientRequest;
import com.vovgoo.BankSystem.dto.client.request.UpdateClientRequest;
import com.vovgoo.BankSystem.dto.client.response.ClientDetailsResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.dto.common.PageParams;
import com.vovgoo.BankSystem.dto.common.PageResponse;
import com.vovgoo.BankSystem.entity.Client;
import com.vovgoo.BankSystem.exception.client.ClientAlreadyExistsException;
import com.vovgoo.BankSystem.exception.client.ClientHasActiveAccountsException;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("ClientServiceImpl — проверка операций с клиентами")
class ClientServiceImplTest {

    @Autowired private ClientServiceImpl clientService;
    @Autowired private ClientRepository clientRepository;
    @Autowired private AccountRepository accountRepository;

    private Client client;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        client = clientRepository.saveAndFlush(new Client("Ivanov", "+375291234567"));
    }

    @Nested
    @DisplayName("Создание клиента")
    class CreateTests {

        @Test
        @DisplayName("Успешное создание нового клиента")
        void shouldCreateClientSuccessfully() {
            CreateClientRequest request = new CreateClientRequest("Petrov", "+375291234568");
            var response = clientService.create(request);

            assertThat(response.getMessage()).contains("успешно создан");
            assertThat(clientRepository.existsByPhone("+375291234568")).isTrue();
        }

        @Test
        @DisplayName("Попытка создания клиента с уже существующим телефоном")
        void shouldThrow_WhenPhoneAlreadyExists() {
            CreateClientRequest request = new CreateClientRequest("Ivanov", "+375291234567");
            assertThatThrownBy(() -> clientService.create(request))
                    .isInstanceOf(ClientAlreadyExistsException.class)
                    .hasMessageContaining("уже существует");
        }
    }

    @Nested
    @DisplayName("Обновление клиента")
    class UpdateTests {

        @Test
        @DisplayName("Успешное обновление данных клиента")
        void shouldUpdateClientSuccessfully() {
            UpdateClientRequest request = new UpdateClientRequest(client.getId(), "Garishness", "+375291234569");
            var response = clientService.update(request);

            assertThat(response.getMessage()).contains("Данные клиента успешно обновлены");
            Client updated = clientRepository.findById(client.getId()).orElseThrow();
            assertThat(updated.getLastName()).isEqualTo("Garishness");
            assertThat(updated.getPhone()).isEqualTo("+375291234569");
        }

        @Test
        @DisplayName("Попытка обновления несуществующего клиента")
        void shouldThrow_WhenClientNotFound() {
            UpdateClientRequest request = new UpdateClientRequest(UUID.randomUUID(), "X", "+375000000000");
            assertThatThrownBy(() -> clientService.update(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Клиент не найден");
        }

        @Test
        @DisplayName("Попытка обновления клиента с уже существующим телефоном")
        void shouldThrow_WhenPhoneAlreadyExists() {
            Client another = clientRepository.saveAndFlush(new Client("Petrov", "+375291234570"));
            UpdateClientRequest request = new UpdateClientRequest(client.getId(), "Ivanov", "+375291234570");
            assertThatThrownBy(() -> clientService.update(request))
                    .isInstanceOf(ClientAlreadyExistsException.class)
                    .hasMessageContaining("уже существует");
        }
    }

    @Nested
    @DisplayName("Удаление клиента")
    class DeleteTests {

        @Test
        @DisplayName("Успешное удаление клиента без активных счетов")
        void shouldDeleteClientSuccessfully_WhenNoAccounts() {
            var response = clientService.delete(client.getId());
            assertThat(response.getMessage()).contains("успешно удалён");
            assertThat(clientRepository.existsById(client.getId())).isFalse();
        }

        @Test
        @DisplayName("Попытка удаления несуществующего клиента")
        void shouldThrow_WhenClientNotFound() {
            assertThatThrownBy(() -> clientService.delete(UUID.randomUUID()))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Клиент не найден");
        }

        @Test
        @DisplayName("Попытка удаления клиента с активными счетами")
        void shouldThrow_WhenClientHasActiveAccounts() {
            accountRepository.saveAndFlush(new com.vovgoo.BankSystem.entity.Account(client));
            assertThatThrownBy(() -> clientService.delete(client.getId()))
                    .isInstanceOf(ClientHasActiveAccountsException.class)
                    .hasMessageContaining("активных счетов");
        }
    }

    @Nested
    @DisplayName("Получение деталей клиента")
    class GetTests {

        @Test
        @DisplayName("Успешное получение информации о клиенте")
        void shouldReturnClientDetails() {
            ClientDetailsResponse details = clientService.get(client.getId());
            assertThat(details.id()).isEqualTo(client.getId());
            assertThat(details.lastName()).isEqualTo(client.getLastName());
            assertThat(details.phone()).isEqualTo(client.getPhone());
        }

        @Test
        @DisplayName("Попытка получения данных несуществующего клиента")
        void shouldThrow_WhenClientNotFound() {
            assertThatThrownBy(() -> clientService.get(UUID.randomUUID()))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Клиент не найден");
        }
    }

    @Nested
    @DisplayName("Поиск клиентов")
    class SearchTests {

        @Test
        @DisplayName("Поиск клиентов с результатами")
        void shouldReturnPagedClients() {
            clientRepository.saveAndFlush(new Client("Petrov", "+375291234571"));
            SearchClientRequest search = new SearchClientRequest("Ivanov");
            PageParams params = new PageParams(0, 10);

            PageResponse<ClientResponse> page = clientService.search(search, params);

            assertThat(page.getContent()).extracting(ClientResponse::lastName)
                    .containsExactly("Ivanov");
        }

        @Test
        @DisplayName("Поиск клиентов без результатов")
        void shouldReturnEmpty_WhenNoMatches() {
            SearchClientRequest search = new SearchClientRequest("NonExistent");
            PageParams params = new PageParams(0, 10);

            PageResponse<ClientResponse> page = clientService.search(search, params);
            assertThat(page.getContent()).isEmpty();
        }
    }
}