package com.bmstu_bureau_1440;

import com.bmstu_bureau_1440.banking.Account;
import com.bmstu_bureau_1440.banking.Bank;
import com.bmstu_bureau_1440.banking.CreditAccount;
import com.bmstu_bureau_1440.banking.Customer;
import com.bmstu_bureau_1440.banking.DebitAccount;
import com.bmstu_bureau_1440.io.Operation;
import com.bmstu_bureau_1440.shared.io.IO;
import com.bmstu_bureau_1440.shared.io.MenuSelector;

public class Main {

    public static void main(String[] args) {
        new Main.BankingApplication().run();
    }

    private final static class BankingApplication extends MenuSelector {

        {
            executors.put(Operation.CREATE_CUSTOMER, () -> {
                var name = IO.inputString("Enter customer's name: ");
                Bank.createCustomer(name);
                IO.displaySuccess("Customer created");
            });

            executors.put(Operation.OPEN_DEBIT, () -> {
                var id = getCustomerIdWithAutocomplete();

                var account = Bank.openDebitAccount(Bank.findCustomer(id));

                IO.displaySuccess(String.format(
                        "Debit account %s for customer %s has been created",
                        account.getAccountNumber(),
                        account.getOwner().getName()));
            });

            executors.put(Operation.OPEN_CREDIT, () -> {
                var id = getCustomerIdWithAutocomplete();
                var creditLimit = IO.inputString("Enter credit limit: ");

                var account = Bank.openCreditAccount(
                        Bank.findCustomer(id),
                        creditLimit != null ? Double.parseDouble(creditLimit) : 0d);

                IO.displaySuccess(String.format(
                        "Credit account %s for customer %s has been created",
                        account.getAccountNumber(),
                        account.getOwner().getName()));
            });

            executors.put(Operation.DEPOSIT, () -> {
                var accountNumber = getAccountNumberWithAutocomplete("Enter account number: ");
                var amount = IO.inputString("Enter deposit amount: ");

                Bank.deposit(accountNumber, amount != null ? Double.parseDouble(amount) : 0d);

                IO.displaySuccess(String.format(
                        "Deposit to account %s for customer %s is successful",
                        accountNumber,
                        Bank.findAccount(accountNumber).getOwner().getName()));
            });

            executors.put(Operation.WITHDRAW, () -> {
                var accountNumber = getAccountNumberWithAutocomplete("Enter account number: ");
                var amount = IO.inputString("Enter withdraw amount: ");

                Bank.withdraw(accountNumber, amount != null ? Double.parseDouble(amount) : 0d);

                IO.displaySuccess(String.format(
                        "Withdraw from account %s for customer %s is successful",
                        accountNumber,
                        Bank.findAccount(accountNumber).getOwner().getName()));
            });

            executors.put(Operation.TRANSFER, () -> {
                var fromAccountNumber = getAccountNumberWithAutocomplete("Enter account number to transfer from: ");
                var toAccountNumber = getAccountNumberWithAutocomplete("Enter account number to transfer to: ");
                var amount = IO.inputString("Enter transfer amount: ");

                Bank.transfer(fromAccountNumber, toAccountNumber, amount != null ? Double.parseDouble(amount) : 0d);

                IO.displaySuccess("Transfer is successful");
            });

            executors.put(Operation.SHOW_ACCOUNTS, () -> {
                var id = getCustomerIdWithAutocomplete();
                Bank.printCustomerAccounts(id);
            });

            executors.put(Operation.SHOW_TRANSACTIONS, () -> {
                Bank.printTransactions();
            });

            executors.put(Operation.SHOW_REPORT, () -> {
                Bank.printReport();
            });
        }

        @Override
        protected boolean loop() {
            return true;
        }

    }

    private static String getCustomerIdWithAutocomplete() {
        return IO.inputWithAutocomplete(
                "Enter customer's ID: ",
                Bank.customers.toArray(Customer[]::new),
                Customer::getId,
                Customer::getName)
                .getId();
    }

    private static String getAccountNumberWithAutocomplete(String label) {
        return IO.inputWithAutocomplete(
                label,
                Bank.accounts.toArray(Account[]::new),
                Account::getAccountNumber,
                (account) -> String.format("%s, %s, %s",
                        account.getOwner().getName(),
                        account instanceof DebitAccount ? "Debit"
                                : account instanceof CreditAccount ? "Credit" : "Unknown",
                        account.getBalance()))
                .getAccountNumber();
    }

}