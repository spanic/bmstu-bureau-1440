package com.bmstu_bureau_1440;

import com.bmstu_bureau_1440.banking.*;
import com.bmstu_bureau_1440.io.IO;
import com.bmstu_bureau_1440.io.Operation;

public class Main {

    public static void main(String[] args) {

        while (true) {
            var option = IO.displayMenu(Operation.values());

            try {
                if (Operation.EXIT.getOperation().equals(option)) {

                    break;

                } else if (Operation.CREATE_CUSTOMER.getOperation().equals(option)) {

                    var name = IO.inputString("Enter customer's name: ");
                    Bank.createCustomer(name);
                    IO.displaySuccess("Customer created");

                } else if (Operation.OPEN_DEBIT.getOperation().equals(option)) {

                    var id = getCustomerIdWithAutocomplete();

                    var account = Bank.openDebitAccount(Bank.findCustomer(id));

                    IO.displaySuccess(String.format(
                            "Debit account %s for customer %s has been created",
                            account.getAccountNumber(),
                            account.getOwner().getName())
                    );

                } else if (Operation.OPEN_CREDIT.getOperation().equals(option)) {

                    var id = getCustomerIdWithAutocomplete();
                    var creditLimit = IO.inputString("Enter credit limit: ");

                    var account = Bank.openCreditAccount(
                            Bank.findCustomer(id),
                            creditLimit != null ? Double.parseDouble(creditLimit) : 0d
                    );

                    IO.displaySuccess(String.format(
                            "Credit account %s for customer %s has been created",
                            account.getAccountNumber(),
                            account.getOwner().getName())
                    );

                } else if (Operation.DEPOSIT.getOperation().equals(option)) {

                    var accountNumber = getAccountNumberWithAutocomplete("Enter account number: ");
                    var amount = IO.inputString("Enter deposit amount: ");

                    Bank.deposit(accountNumber, amount != null ? Double.parseDouble(amount) : 0d);

                    IO.displaySuccess(String.format(
                            "Deposit to account %s for customer %s is successful",
                            accountNumber,
                            Bank.findAccount(accountNumber).getOwner().getName())
                    );

                } else if (Operation.WITHDRAW.getOperation().equals(option)) {

                    var accountNumber = getAccountNumberWithAutocomplete("Enter account number: ");
                    var amount = IO.inputString("Enter withdraw amount: ");

                    Bank.withdraw(accountNumber, amount != null ? Double.parseDouble(amount) : 0d);

                    IO.displaySuccess(String.format(
                            "Withdraw from account %s for customer %s is successful",
                            accountNumber,
                            Bank.findAccount(accountNumber).getOwner().getName())
                    );

                } else if (Operation.TRANSFER.getOperation().equals(option)) {

                    var fromAccountNumber = getAccountNumberWithAutocomplete("Enter account number to transfer from: ");
                    var toAccountNumber = getAccountNumberWithAutocomplete("Enter account number to transfer to: ");
                    var amount = IO.inputString("Enter transfer amount: ");

                    Bank.transfer(fromAccountNumber, toAccountNumber, amount != null ? Double.parseDouble(amount) : 0d);

                    IO.displaySuccess("Transfer is successful");

                } else if (Operation.SHOW_ACCOUNTS.getOperation().equals(option)) {

                    var id = getCustomerIdWithAutocomplete();
                    Bank.printCustomerAccounts(id);

                } else if (Operation.SHOW_TRANSACTIONS.getOperation().equals(option)) {
                    Bank.printTransactions();

                } else if (Operation.SHOW_REPORT.getOperation().equals(option)) {
                    Bank.printReport();
                }

            } catch (Exception e) {
                IO.displayError(e);
            }

        }

    }

    private static String getCustomerIdWithAutocomplete() {
        return IO.inputWithAutocomplete(
                "Enter customer's ID: ",
                Bank.customers.toArray(Customer[]::new),
                Customer::getId,
                Customer::getName
        );
    }

    private static String getAccountNumberWithAutocomplete(String label) {
        return IO.inputWithAutocomplete(
                label,
                Bank.accounts.toArray(Account[]::new),
                Account::getAccountNumber,
                (account) -> String.format("%s, %s, %s",
                        account.getOwner().getName(),
                        account instanceof DebitAccount ? "Debit" : account instanceof CreditAccount ? "Credit" : "Unknown",
                        account.getBalance()
                )
        );
    }

}