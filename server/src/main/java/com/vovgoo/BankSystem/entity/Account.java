package com.vovgoo.BankSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@ToString(exclude = "client")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Владелец счета обязателен")
    private Client client;

    @Column(name = "balance", precision = 19, scale = 2, nullable = false)
    @NotNull(message = "Баланс не может быть пустым")
    @DecimalMin(value = "0.00", message = "Баланс не может быть отрицательным")
    private BigDecimal balance;

    public Account(Client client) {
        if (client == null) throw new IllegalArgumentException("Клиент обязателен для открытия счета");
        this.client = client;
        this.balance = BigDecimal.ZERO;
    }

    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть положительной");
        }
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма снятия должна быть положительной");
        }
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Недостаточно средств");
        }
        balance = balance.subtract(amount);
    }
}