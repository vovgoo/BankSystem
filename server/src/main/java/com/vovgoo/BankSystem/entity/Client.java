package com.vovgoo.BankSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clients")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "accounts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Фамилия не должна быть пустой")
    @Setter
    private String lastName;

    @Column(name = "phone", nullable = false)
    @NotBlank(message = "Телефон обязателен")
    @Pattern(
            regexp = "^\\+375\\d{9}$",
            message = "Телефон должен быть в формате +375XXXXXXXXX"
    )
    @Setter
    private String phone;

    @OneToMany(
            mappedBy = "client",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    @Setter
    private List<Account> accounts = new ArrayList<>();

    public void addAccount(Account account) {
        accounts.add(account);
        account.setClient(this);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }
}