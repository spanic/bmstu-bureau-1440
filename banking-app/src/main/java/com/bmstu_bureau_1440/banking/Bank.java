package com.bmstu_bureau_1440.banking;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Bank {

    public static final List<Customer> customers = new ArrayList<>();
    public static final List<Account> accounts = new ArrayList<>();
    public static final List<Transaction> transactions = new ArrayList<>();

    public static Customer createCustomer(String name) throws NullPointerException {
        Customer customer = new Customer(name);
        customers.add(customer);
        return customer;
    }

    public static Customer findCustomer(String id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public static Account openDebitAccount(Customer owner) throws NullPointerException {
        Account debitAccount = new DebitAccount(owner);
        accounts.add(debitAccount);
        return debitAccount;
    }

    public static Account openCreditAccount(Customer owner, double creditLimit) throws NullPointerException {
        Account creditAccount = new CreditAccount(owner, creditLimit);
        accounts.add(creditAccount);
        return creditAccount;
    }

    public static Account findAccount(String accountNumber) {
        return accounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public static boolean deposit(String accountNumber, double amount) {
        var transaction = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .toAccountNumber(accountNumber)
                .amount(amount);

        return execute(() -> {
            var account = findAccount(accountNumber);
            return account.deposit(amount);
        }, transaction);
    }

    public static boolean withdraw(String accountNumber, double amount) {
        var transaction = Transaction.builder()
                .type(TransactionType.WITHDRAWAL)
                .fromAccountNumber(accountNumber)
                .amount(amount);

        return execute(() -> {
            var account = findAccount(accountNumber);
            return account.withdraw(amount);
        }, transaction);
    }

    public static boolean transfer(String from, String to, double amount) {
        var transaction = Transaction.builder()
                .type(TransactionType.TRANSFER)
                .fromAccountNumber(from)
                .toAccountNumber(to)
                .amount(amount);

        return execute(
                () -> {
                    var fromAccount = findAccount(from);
                    var toAccount = findAccount(to);
                    return fromAccount.transfer(toAccount, amount);
                }, transaction
        );
    }

    public static void printCustomerAccounts(String customerId) {
        accounts.stream()
                .filter(account -> account.getOwner().getId().equals(customerId))
                .forEach(System.out::println);
    }

    public static void printTransactions() {
        transactions.forEach(System.out::println);
    }

    public static void printReport() {
        int debitAccounts = 0, creditAccounts = 0;
        double debitAccountsSum = 0.0, creditAccountsSum = 0.0;

        for (var account : accounts) {
            if (account instanceof DebitAccount) {
                debitAccounts++;
                debitAccountsSum += account.getBalance();
            } else if (account instanceof CreditAccount) {
                creditAccounts++;
                creditAccountsSum += account.getBalance();
            }
        }

        int successfulTransactions = 0, failedTransactions = 0;

        for (var transaction : transactions) {
            if (transaction.isSuccess()) {
                successfulTransactions++;
            } else {
                failedTransactions++;
            }
        }

        System.out.println("Total debit accounts: " + debitAccounts + "; total balance: " + debitAccountsSum);
        System.out.println("Total credit accounts: " + creditAccounts + "; total balance: " + creditAccountsSum);
        System.out.println("Transactions: successful - " + successfulTransactions + "; failed - " + failedTransactions);
    }

    private static boolean execute(Supplier<Boolean> action, Transaction.TransactionBuilder transaction) {

        var success = false;

        try {
            success = action.get();
        } catch (Exception e) {
            transaction.message(e.getMessage());
            throw e;
        } finally {
            transaction.success(success);
            if (success) transaction.message(Transaction.TRANSACTION_SUCCESS_MESSAGE);
            transactions.add(transaction.build());
        }

        return success;

    }

}
