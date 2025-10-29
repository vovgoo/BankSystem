package com.vovgoo.BankSystem.exception.account;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Недостаточно средств для снятия");
    }
}