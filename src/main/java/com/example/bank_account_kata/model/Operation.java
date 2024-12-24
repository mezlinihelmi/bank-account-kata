package com.example.bank_account_kata.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Operation {
    private final LocalDate date;
    private final OperationType type;
    private final double amount;
    private final double balance;
}
