package com.example.bank_account_kata.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OperationDTO {
    private LocalDate date;
    private String type;
    private double amount;
    private double balance;
}
