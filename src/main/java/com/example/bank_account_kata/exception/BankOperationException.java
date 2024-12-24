package com.example.bank_account_kata.exception;

public class BankOperationException extends RuntimeException {
    public BankOperationException(String message) {
        super(message);
    }
}
