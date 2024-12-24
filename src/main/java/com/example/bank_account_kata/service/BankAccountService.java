package com.example.bank_account_kata.service;

import com.example.bank_account_kata.dto.OperationDTO;
import com.example.bank_account_kata.model.BankAccount;
import com.example.bank_account_kata.model.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccount bankAccount = new BankAccount();

    public void deposit(double amount) {
        bankAccount.deposit(amount);
    }

    public void withdraw(double amount) {
        bankAccount.withdraw(amount);
    }

    public double getBalance() {
        return bankAccount.getBalance();
    }

    public List<OperationDTO> getOperations() {
        return bankAccount.getOperations()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OperationDTO convertToDTO(Operation operation) {
        return OperationDTO.builder()
                .date(operation.getDate())
                .type(operation.getType().toString())
                .amount(operation.getAmount())
                .balance(operation.getBalance())
                .build();
    }
}
