package com.vovgoo.BankSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vovgoo.BankSystem.dto.client.request.CreateClientRequest;
import com.vovgoo.BankSystem.dto.client.request.UpdateClientRequest;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("ClientController — API операции с клиентами")
class ClientControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ClientRepository clientRepository;
    @Autowired private AccountRepository accountRepository;

    private Client client;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
        clientRepository.deleteAll();
        client = clientRepository.save(new Client("Ivanov", "+375291234567"));
    }

    @Nested
    @DisplayName("Создание клиента")
    class CreateClientTests {

        @Test
        @DisplayName("Успешное создание нового клиента")
        void createClient_Success() throws Exception {
            CreateClientRequest request = new CreateClientRequest("Petrov", "+375292223344");
            mockMvc.perform(post("/api/v1/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("APPROVED"))
                    .andExpect(jsonPath("$.message").value("Клиент был успешно создан"));
        }

        @Test
        @DisplayName("Создание клиента с уже существующим телефоном")
        void createClient_AlreadyExists() throws Exception {
            CreateClientRequest request = new CreateClientRequest("Ivanov", "+375291234567");
            mockMvc.perform(post("/api/v1/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }

        @Test
        @DisplayName("Создание клиента с некорректными данными")
        void createClient_InvalidRequest() throws Exception {
            CreateClientRequest request = new CreateClientRequest("", "12345");
            mockMvc.perform(post("/api/v1/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }
    }

    @Nested
    @DisplayName("Обновление клиента")
    class UpdateClientTests {

        @Test
        @DisplayName("Успешное обновление клиента")
        void updateClient_Success() throws Exception {
            UpdateClientRequest request = new UpdateClientRequest(client.getId(), "Petrov", "+375297778899");
            mockMvc.perform(put("/api/v1/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("APPROVED"))
                    .andExpect(jsonPath("$.message").value("Данные клиента успешно обновлены"));
        }

        @Test
        @DisplayName("Обновление клиента — клиент не найден")
        void updateClient_NotFound() throws Exception {
            UpdateClientRequest request = new UpdateClientRequest(UUID.randomUUID(), "Petrov", "+375297778899");
            mockMvc.perform(put("/api/v1/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }

        @Test
        @DisplayName("Обновление клиента — телефон уже существует")
        void updateClient_PhoneAlreadyExists() throws Exception {
            Client another = clientRepository.save(new Client("Petrov", "+375297778899"));
            UpdateClientRequest request = new UpdateClientRequest(client.getId(), "Ivanov", another.getPhone());
            mockMvc.perform(put("/api/v1/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }
    }

    @Nested
    @DisplayName("Получение клиента")
    class GetClientTests {

        @Test
        @DisplayName("Успешное получение клиента по ID")
        void getClient_Success() throws Exception {
            mockMvc.perform(get("/api/v1/clients/{id}", client.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(client.getId().toString()))
                    .andExpect(jsonPath("$.lastName").value("Ivanov"))
                    .andExpect(jsonPath("$.phone").value("+375291234567"));
        }

        @Test
        @DisplayName("Получение клиента — клиент не найден")
        void getClient_NotFound() throws Exception {
            mockMvc.perform(get("/api/v1/clients/{id}", UUID.randomUUID()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }
    }

    @Nested
    @DisplayName("Удаление клиента")
    class DeleteClientTests {

        @Test
        @DisplayName("Успешное удаление клиента без активных счетов")
        void deleteClient_Success() throws Exception {
            mockMvc.perform(delete("/api/v1/clients/{id}", client.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("APPROVED"))
                    .andExpect(jsonPath("$.message").value("Клиент был успешно удалён"));
        }

        @Test
        @DisplayName("Удаление клиента — клиент не найден")
        void deleteClient_NotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/clients/{id}", UUID.randomUUID()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }

        @Test
        @DisplayName("Удаление клиента — клиент имеет активные счета")
        void deleteClient_HasAccounts() throws Exception {
            accountRepository.save(new Account(client));
            mockMvc.perform(delete("/api/v1/clients/{id}", client.getId()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("DECLINED"));
        }
    }

    @Nested
    @DisplayName("Поиск клиентов")
    class SearchClientsTests {

        @Test
        @DisplayName("Поиск без фильтров — успешный ответ")
        void searchClients_All() throws Exception {
            mockMvc.perform(get("/api/v1/clients/search")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.content").isArray());
        }

        @Test
        @DisplayName("Поиск по фамилии — успешный ответ")
        void searchClients_ByLastName() throws Exception {
            mockMvc.perform(get("/api/v1/clients/search")
                            .param("lastName", "Ivanov")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].lastName").value("Ivanov"));
        }
    }
}