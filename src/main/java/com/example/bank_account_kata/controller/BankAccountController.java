package com.example.bank_account_kata.controller;

import com.example.bank_account_kata.dto.DepositRequestDTO;
import com.example.bank_account_kata.dto.OperationDTO;
import com.example.bank_account_kata.dto.WithdrawRequestDTO;
import com.example.bank_account_kata.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService service;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequestDTO request) {
        service.deposit(request.getAmount());
        return ResponseEntity.ok("Deposit successful. Current balance: " + service.getBalance());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequestDTO request) {
        service.withdraw(request.getAmount());
        return ResponseEntity.ok("Withdrawal successful. Current balance: " + service.getBalance());
    }

    @GetMapping("/operations")
    public ResponseEntity<List<OperationDTO>> getOperations() {
        return ResponseEntity.ok(service.getOperations());
    }

    @GetMapping("/balance")
    public ResponseEntity<String> getBalance() {
        return ResponseEntity.ok("Current balance: " + service.getBalance());
    }
}
