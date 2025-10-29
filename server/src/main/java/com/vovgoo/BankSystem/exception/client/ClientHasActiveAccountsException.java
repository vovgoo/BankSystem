package com.vovgoo.BankSystem.exception.client;

public class ClientHasActiveAccountsException extends RuntimeException {
  public ClientHasActiveAccountsException(long count) {
    super(String.format("Клиент имеет %d активных счетов и не может быть удалён", count));
  }
}