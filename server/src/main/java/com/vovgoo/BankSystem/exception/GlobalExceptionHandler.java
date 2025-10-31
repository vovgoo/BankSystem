package com.vovgoo.BankSystem.exception;

import com.vovgoo.BankSystem.dto.transaction.TransactionResponse;
import com.vovgoo.BankSystem.exception.account.AccountBalanceNotZeroException;
import com.vovgoo.BankSystem.exception.account.InsufficientFundsException;
import com.vovgoo.BankSystem.exception.account.SameAccountTransferException;
import com.vovgoo.BankSystem.exception.client.ClientAlreadyExistsException;
import com.vovgoo.BankSystem.exception.client.ClientHasActiveAccountsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<TransactionResponse> handleClientAlreadyExists(ClientAlreadyExistsException ex) {
        TransactionResponse resp = TransactionResponse.decline(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(resp);
    }

    @ExceptionHandler(ClientHasActiveAccountsException.class)
    public ResponseEntity<TransactionResponse> handleClientHasActiveAccounts(ClientHasActiveAccountsException ex) {
        TransactionResponse resp = TransactionResponse.decline(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp);
    }

    @ExceptionHandler(AccountBalanceNotZeroException.class)
    public ResponseEntity<TransactionResponse> handleAccountBalanceNotZero(AccountBalanceNotZeroException ex) {
        TransactionResponse resp = TransactionResponse.decline(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<TransactionResponse> handleInsufficientFunds(InsufficientFundsException ex) {
        TransactionResponse resp = TransactionResponse.decline(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp);
    }

    @ExceptionHandler(SameAccountTransferException.class)
    public ResponseEntity<TransactionResponse> handleSameAccountTransfer(SameAccountTransferException ex) {
        TransactionResponse resp = TransactionResponse.decline(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<TransactionResponse> handleEntityNotFound(EntityNotFoundException ex) {
        TransactionResponse resp = TransactionResponse.decline(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(resp);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<TransactionResponse> handleIllegalArgument(IllegalArgumentException ex) {
        TransactionResponse resp = TransactionResponse.decline(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TransactionResponse> handleAll(Exception ex) {
        TransactionResponse resp = TransactionResponse.decline("Внутренняя ошибка сервиса");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(resp);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TransactionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }

        TransactionResponse resp = TransactionResponse.decline(errors.toString());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<TransactionResponse> handleOptimisticLock(OptimisticLockException ex) {
        TransactionResponse resp = TransactionResponse.decline(
                "Конфликт при параллельной операции. Попробуйте повторить действие."
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(resp);
    }
}