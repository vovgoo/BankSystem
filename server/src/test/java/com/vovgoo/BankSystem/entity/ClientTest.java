package com.vovgoo.BankSystem.entity;

import org.junit.jupiter.api.Test;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

class ClientTest {

    @Test
    void constructor_ShouldThrow_WhenLastNameIsNull() {
        assertThatThrownBy(() -> new Client(null, "+375291234567"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Фамилия обязательна для создания клиента");
    }

    @Test
    void constructor_ShouldThrow_WhenLastNameIsBlank() {
        assertThatThrownBy(() -> new Client("  ", "+375291234567"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Фамилия обязательна для создания клиента");
    }

    @Test
    void constructor_ShouldThrow_WhenPhoneIsNull() {
        assertThatThrownBy(() -> new Client("Ivanov", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Телефон обязателен для создания клиента");
    }

    @Test
    void constructor_ShouldThrow_WhenPhoneIsBlank() {
        assertThatThrownBy(() -> new Client("Ivanov", " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Телефон обязателен для создания клиента");
    }

    @Test
    void constructor_ShouldThrow_WhenLastNameAndPhoneAreNull() {
        assertThatThrownBy(() -> new Client(null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Фамилия обязательна для создания клиента");
    }

    @Test
    void constructor_ShouldInitializeAccountsList() {
        Client client = new Client("Ivanov", "+375291234567");
        assertThat(client.getLastName()).isEqualTo("Ivanov");
        assertThat(client.getPhone()).isEqualTo("+375291234567");
        assertThat(client.getAccounts()).isEmpty();
    }

    @Test
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
    void setLastName_ShouldUpdate_WhenValid() {
        Client client = new Client("Ivanov", "+375291234567");
        client.setLastName("Petrov");
        assertThat(client.getLastName()).isEqualTo("Petrov");
    }

    @Test
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
    void setPhone_ShouldUpdate_WhenValid() {
        Client client = new Client("Ivanov", "+375291234567");
        client.setPhone("+375441112233");
        assertThat(client.getPhone()).isEqualTo("+375441112233");
    }

    @Test
    void getAccounts_ShouldReturnUnmodifiableList() {
        Client client = new Client("Ivanov", "+375291234567");
        assertThat(client.getAccounts()).isInstanceOf(Collections.unmodifiableList(new java.util.ArrayList<>()).getClass());
    }
}