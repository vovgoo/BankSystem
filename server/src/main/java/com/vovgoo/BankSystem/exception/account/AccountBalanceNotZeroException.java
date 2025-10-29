package com.vovgoo.BankSystem.exception.account;

public class AccountBalanceNotZeroException extends RuntimeException {
    public AccountBalanceNotZeroException() {
        super("Невозможно удалить счёт с ненулевым балансом");
    }
}