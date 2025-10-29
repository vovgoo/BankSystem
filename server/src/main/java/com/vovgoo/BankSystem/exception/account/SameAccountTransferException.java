package com.vovgoo.BankSystem.exception.account;

public class SameAccountTransferException extends RuntimeException {
    public SameAccountTransferException() {
        super("Нельзя перевести средства на тот же счёт");
    }
}