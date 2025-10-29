package com.vovgoo.BankSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clients")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "accounts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Getter
    private UUID id;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Фамилия не должна быть пустой")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яЁё]+(-[A-Za-zА-Яа-яЁё]+)?$",
            message = "Фамилия должна быть одним словом или двойная через тире"
    )
    @Getter
    private String lastName;

    @Column(name = "phone", nullable = false, unique = true)
    @NotBlank(message = "Телефон обязателен")
    @Pattern(
            regexp = "^\\+375\\d{9}$",
            message = "Телефон должен быть в формате +375XXXXXXXXX"
    )
    @Getter
    private String phone;

    public Client(String lastName, String phone) {
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Фамилия обязательна для создания клиента");
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("Телефон обязателен для создания клиента");
        this.lastName = lastName;
        this.phone = phone;
    }

    @OneToMany(
            mappedBy = "client",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<Account> accounts = new ArrayList<>();

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public void updateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Фамилия обязательна");
        this.lastName = lastName;
    }

    public void updatePhone(String phone) {
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("Телефон обязателен");
        this.phone = phone;
    }

    public void addAccount(Account account) {
        accounts.add(account);
        account.setClient(this);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }
}