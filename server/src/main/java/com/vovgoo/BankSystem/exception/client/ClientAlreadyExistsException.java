package com.vovgoo.BankSystem.exception.client;

public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException(String phone) {
        super("Клиент с номером телефона " + phone + " уже существует");
    }
}