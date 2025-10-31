package com.vovgoo.BankSystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import jakarta.validation.*;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("Client — проверка валидации и методов")
class ClientTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Конструктор")
    class ConstructorTests {

        @Test
        @DisplayName("Фамилия null")
        void constructor_ShouldHaveViolation_WhenLastNameIsNull() {
            Client client = new Client(null, "+375291234567");
            Set<ConstraintViolation<Client>> violations = validator.validate(client);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
        }

        @Test
        @DisplayName("Фамилия пустая")
        void constructor_ShouldHaveViolation_WhenLastNameIsBlank() {
            Client client = new Client(" ", "+375291234567");
            Set<ConstraintViolation<Client>> violations = validator.validate(client);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
        }

        @Test
        @DisplayName("Телефон null")
        void constructor_ShouldHaveViolation_WhenPhoneIsNull() {
            Client client = new Client("Ivanov", null);
            Set<ConstraintViolation<Client>> violations = validator.validate(client);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("phone"));
        }

        @Test
        @DisplayName("Телефон пустой")
        void constructor_ShouldHaveViolation_WhenPhoneIsBlank() {
            Client client = new Client("Ivanov", " ");
            Set<ConstraintViolation<Client>> violations = validator.validate(client);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("phone"));
        }

        @Test
        @DisplayName("Фамилия и телефон null")
        void constructor_ShouldHaveViolation_WhenLastNameAndPhoneAreNull() {
            Client client = new Client(null, null);
            Set<ConstraintViolation<Client>> violations = validator.validate(client);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("phone"));
        }

        @Test
        @DisplayName("Инициализация списка аккаунтов")
        void constructor_ShouldInitializeAccountsList() {
            Client client = new Client("Ivanov", "+375291234567");
            assertThat(client.getAccounts()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Методы setLastName")
    class SetLastNameTests {

        @Test
        @DisplayName("Валидация пустой фамилии")
        void setLastName_ShouldHaveViolation_WhenBlank() {
            Client client = new Client("Ivanov", "+375291234567");
            client.setLastName(" ");
            Set<ConstraintViolation<Client>> violations = validator.validate(client);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
        }

        @Test
        @DisplayName("Обновление валидной фамилии")
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
        @DisplayName("Валидация пустого телефона")
        void setPhone_ShouldHaveViolation_WhenBlank() {
            Client client = new Client("Ivanov", "+375291234567");
            client.setPhone(" ");
            Set<ConstraintViolation<Client>> violations = validator.validate(client);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("phone"));
        }

        @Test
        @DisplayName("Обновление валидного телефона")
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