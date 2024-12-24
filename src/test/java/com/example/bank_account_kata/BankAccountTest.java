package com.example.bank_account_kata;

import com.example.bank_account_kata.exception.BankOperationException;
import com.example.bank_account_kata.model.BankAccount;
import com.example.bank_account_kata.model.Operation;
import com.example.bank_account_kata.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    private BankAccount bankAccount;

    @BeforeEach
    void setUp() {
        bankAccount = new BankAccount();
    }

    @Test
    void testDeposit() {
        bankAccount.deposit(500);
        assertEquals(500, bankAccount.getBalance(), "Balance should be 500 after deposit.");
    }

    @Test
    void testWithdraw() {
        bankAccount.deposit(500);
        bankAccount.withdraw(200);
        assertEquals(300, bankAccount.getBalance(), "Balance should be 300 after withdrawal.");
    }

    @Test
    void testWithdrawInsufficientFunds() {
        bankAccount.deposit(100);
        Exception exception = assertThrows(BankOperationException.class, () -> bankAccount.withdraw(200));
        assertEquals("Insufficient funds", exception.getMessage());
    }

    @Test
    void testDepositNegativeAmount() {
        Exception exception = assertThrows(BankOperationException.class, () -> bankAccount.deposit(-50));
        assertEquals("Deposit amount must be positive", exception.getMessage());
    }

    @Test
    void testWithdrawNegativeAmount() {
        Exception exception = assertThrows(BankOperationException.class, () -> bankAccount.withdraw(-50));
        assertEquals("Withdrawal amount must be positive", exception.getMessage());
    }

    @Test
    void testOperationsHistory() {
        bankAccount.deposit(500);
        bankAccount.withdraw(200);
        List<Operation> operations = bankAccount.getOperations();

        assertEquals(2, operations.size(), "There should be two operations in the history.");
        assertEquals(OperationType.DEPOSIT, operations.get(0).getType());
        assertEquals(OperationType.WITHDRAWAL, operations.get(1).getType());
    }

    @Test
    void testConcurrentDeposits() throws InterruptedException {
        int threadCount = 10; // Nombre de threads concurrents
        int depositCountPerThread = 1000; // Nombre de dépôts par thread
        double depositAmount = 10.0; // Montant de chaque dépôt

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        Runnable depositTask = () -> {
            for (int i = 0; i < depositCountPerThread; i++) {
                bankAccount.deposit(depositAmount);
            }
        };

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(depositTask);
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        double expectedBalance = threadCount * depositCountPerThread * depositAmount;

        System.out.println("Final balance after deposits: " + bankAccount.getBalance());
        assertEquals(expectedBalance, bankAccount.getBalance(),
                "The balance should match the total deposits made by all threads.");

        List<Operation> operations = bankAccount.getOperations();
        assertEquals(threadCount * depositCountPerThread, operations.size(),
                "The total number of operations should match the deposits performed.");
    }

    @Test
    void testConcurrentWithdrawals() throws InterruptedException {

        double initialBalance = 100_000.0;
        bankAccount.deposit(initialBalance);

        int threadCount = 10;
        int withdrawCountPerThread = 500;
        double withdrawAmount = 10.0;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        Runnable withdrawTask = () -> {
            for (int i = 0; i < withdrawCountPerThread; i++) {
                try {
                    bankAccount.withdraw(withdrawAmount);
                } catch (Exception e) {
                    System.err.println("Withdrawal failed: " + e.getMessage());
                }
            }
        };

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(withdrawTask);
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        double expectedBalance = initialBalance - (threadCount * withdrawCountPerThread * withdrawAmount);

        System.out.println("Final balance after withdrawals: " + bankAccount.getBalance());
        assertEquals(expectedBalance, bankAccount.getBalance(),
                "The balance should match the total withdrawals performed by all threads.");

        List<Operation> operations = bankAccount.getOperations();
        assertEquals(threadCount * withdrawCountPerThread + 1, operations.size(), // +1 pour la premiere operation de deposit
                "The total number of operations should match the withdrawals performed.");
    }




}
