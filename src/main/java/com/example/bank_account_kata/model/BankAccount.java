package com.example.bank_account_kata.model;

import com.example.bank_account_kata.exception.BankOperationException;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class BankAccount {

    private double balance;
    private final List<Operation> operations;
    private final Lock lock = new ReentrantLock();

    public BankAccount() {
        this.balance = 0.0;
        this.operations = new ArrayList<>();
    }

    public void deposit(double amount) {
        lock.lock();
        try {
            if (amount <= 0) {
                throw new BankOperationException("Deposit amount must be positive");
            } else {
                System.out.println(Thread.currentThread().getName() + " - Depositing: " + amount);
                balance += amount;
                operations.add(new Operation(LocalDate.now(), OperationType.DEPOSIT, amount, balance));
                System.out.println(Thread.currentThread().getName() + " - New Balance: " + balance);
            }
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) {
        lock.lock();
        if (amount <= 0) {
            throw new BankOperationException("Withdrawal amount must be positive");
        }
        try {
            if (amount > balance) {
                throw new BankOperationException("Insufficient funds");
            } else {
                balance -= amount;
                operations.add(new Operation(LocalDate.now(), OperationType.WITHDRAWAL, amount, balance));
            }
        } finally {
            lock.unlock();
        }
    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public List<Operation> getOperations() {
        lock.lock();
        try {
            return Collections.unmodifiableList(new ArrayList<>(operations));
        } finally {
            lock.unlock();
        }
    }
}
