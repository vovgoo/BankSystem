package com.vovgoo.BankSystem.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DisplayName("Client — проверка валидации и методов")
class ClientTest {

    @Nested
    @DisplayName("Конструктор")
    class ConstructorTests {

        @Test
        @DisplayName("Бросает исключение, если фамилия null")
        void constructor_ShouldThrow_WhenLastNameIsNull() {
            assertThatThrownBy(() -> new Client(null, "+375291234567"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Фамилия обязательна для создания клиента");
        }

        @Test
        @DisplayName("Бросает исключение, если фамилия пустая")
        void constructor_ShouldThrow_WhenLastNameIsBlank() {
            assertThatThrownBy(() -> new Client("  ", "+375291234567"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Фамилия обязательна для создания клиента");
        }

        @Test
        @DisplayName("Бросает исключение, если телефон null")
        void constructor_ShouldThrow_WhenPhoneIsNull() {
            assertThatThrownBy(() -> new Client("Ivanov", null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Телефон обязателен для создания клиента");
        }

        @Test
        @DisplayName("Бросает исключение, если телефон пустой")
        void constructor_ShouldThrow_WhenPhoneIsBlank() {
            assertThatThrownBy(() -> new Client("Ivanov", " "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Телефон обязателен для создания клиента");
        }

        @Test
        @DisplayName("Бросает исключение, если фамилия и телефон null")
        void constructor_ShouldThrow_WhenLastNameAndPhoneAreNull() {
            assertThatThrownBy(() -> new Client(null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Фамилия обязательна для создания клиента");
        }

        @Test
        @DisplayName("Инициализирует список аккаунтов пустым")
        void constructor_ShouldInitializeAccountsList() {
            Client client = new Client("Ivanov", "+375291234567");
            assertThat(client.getLastName()).isEqualTo("Ivanov");
            assertThat(client.getPhone()).isEqualTo("+375291234567");
            assertThat(client.getAccounts()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Методы setLastName")
    class SetLastNameTests {

        @Test
        @DisplayName("Бросает исключение при null или пустой фамилии")
        void setLastName_ShouldThrow_WhenNullOrBlank() {
            Client client = new Client("Ivanov", "+375291234567");

            assertThatThrownBy(() -> client.setLastName(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Фамилия обязательна");

            assertThatThrownBy(() -> client.setLastName(" "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Фамилия обязательна");
        }

        @Test
        @DisplayName("Обновляет фамилию, если значение корректное")
        void setLastName_ShouldUpdate_WhenValid() {
            Client client = new Client("Ivanov", "+375291234567");
            client.setLastName("Petrov");
            assertThat(client.getLastName()).isEqualTo("Petrov");
        }
    }

    @Nested
    @DisplayName("Методы setPhone")
    class SetPhoneTests {

        @Test
        @DisplayName("Бросает исключение при null или пустом телефоне")
        void setPhone_ShouldThrow_WhenNullOrBlank() {
            Client client = new Client("Ivanov", "+375291234567");

            assertThatThrownBy(() -> client.setPhone(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Телефон обязателен");

            assertThatThrownBy(() -> client.setPhone(" "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Телефон обязателен");
        }

        @Test
        @DisplayName("Обновляет телефон, если значение корректное")
        void setPhone_ShouldUpdate_WhenValid() {
            Client client = new Client("Ivanov", "+375291234567");
            client.setPhone("+375441112233");
            assertThat(client.getPhone()).isEqualTo("+375441112233");
        }
    }

    @Nested
    @DisplayName("Методы getAccounts")
    class GetAccountsTests {

        @Test
        @DisplayName("Возвращает неизменяемый список аккаунтов")
        void getAccounts_ShouldReturnUnmodifiableList() {
            Client client = new Client("Ivanov", "+375291234567");
            assertThat(client.getAccounts())
                    .isInstanceOf(Collections.unmodifiableList(new java.util.ArrayList<>()).getClass());
        }
    }
}