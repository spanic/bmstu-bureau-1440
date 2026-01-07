package banking;

import com.bmstu_bureau_1440.banking.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BankTests {

    private static Customer customer;

    @BeforeAll
    static void setup() {
        customer = Bank.createCustomer("John Doe");
    }

    @Test
    @DisplayName("Should create customer")
    void createCustomer() {
        assertNotNull(customer, "Customer should not be null");
        assertInstanceOf(Customer.class, customer, "Customer should be an instance of Customer");
        assertTrue(Bank.customers.contains(customer), "Customer should be in the list");
    }

    @Test
    @DisplayName("Should find customer by id")
    void findCustomer() {
        var retrievedCustomer = assertDoesNotThrow(() -> Bank.findCustomer(customer.getId()), "Should find customer by id");
        assertEquals(retrievedCustomer, customer, "Found customer should be the same as the created customer");
    }

    @Test
    @DisplayName("Should throw exception when customer is not found")
    void throwIfCustomerNotFound() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Bank.findCustomer(null),
                "Should throw exception when customer is not found"
        );
    }

    @Test
    @DisplayName("Should open debit account")
    void openDebitAccount() {
        var debitAccount = Bank.openDebitAccount(customer);

        assertNotNull(debitAccount, "Debit account should not be null");
        assertInstanceOf(DebitAccount.class, debitAccount, "Debit account should be an instance of DebitAccount");
        assertTrue(Bank.accounts.contains(debitAccount), "Account should be in the list");
    }

    @Test
    @DisplayName("Should open credit account")
    void openCreditAccount() {
        var creditAccount = Bank.openCreditAccount(customer, 1000d);

        assertNotNull(creditAccount, "Credit account should not be null");
        assertInstanceOf(CreditAccount.class, creditAccount, "Credit account should be an instance of CreditAccount");
        assertTrue(Bank.accounts.contains(creditAccount), "Account should be in the list");
    }

    @Test
    @DisplayName("Should find account by account number")
    void findAccount() {
        var account = Bank.openCreditAccount(customer, 250d);

        var retrievedAccount = assertDoesNotThrow(
                () -> Bank.findAccount(account.getAccountNumber()),
                "Should find retrievedAccount by retrievedAccount number"
        );
        assertEquals(retrievedAccount, account, "Found retrievedAccount should be the same as the created retrievedAccount");
    }

    @Test
    @DisplayName("Should throw exception when account is not found")
    void throwIfAccountNotFound() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Bank.findAccount(null),
                "Should throw exception when account is not found"
        );
    }

    @ParameterizedTest()
    @DisplayName("Should deposit money to account")
    @MethodSource("accountsProvider")
    void depositToAccount(Account account, double amount) {
        var currentAmountOfTransactions = Bank.transactions.size();
        boolean result = false;

        if (amount > 0) {
            result = Boolean.TRUE.equals(assertDoesNotThrow(
                    () -> Bank.deposit(account.getAccountNumber(), amount),
                    "Should not throw exception when making a deposit"
            ));

            assertEquals(Boolean.TRUE, result, "Should deposit money to account");
            assertEquals(amount, account.getBalance(), "Balance should be increased by the amount of money deposited");
        } else {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Bank.deposit(account.getAccountNumber(), amount),
                    "Should throw exception when amount is less than or equal to 0"
            );
            assertEquals(0d, account.getBalance(), "Balance should not be changed when amount is less than or equal to 0");
        }

        assertEquals(currentAmountOfTransactions + 1, Bank.transactions.size(), "Should increase amount of transactions by 1");

        var transaction = Bank.transactions.getLast();

        assertEquals(TransactionType.DEPOSIT, transaction.getType(), "Transaction type should be DEPOSIT");
        assertEquals(amount, transaction.getAmount(), "Transaction should have correct amount of money");
        assertEquals(account.getAccountNumber(), transaction.getToAccountNumber(), "Transaction should have correct from account number");
        assertEquals(result, transaction.isSuccess(), "Transaction should have correct success status");
        assertTrue(result ? Transaction.TRANSACTION_SUCCESS_MESSAGE.equals(transaction.getMessage()) : !transaction.getMessage().isEmpty(), "Transaction should have correct message");
    }

    @ParameterizedTest
    @DisplayName("Should withdraw money from account")
    @MethodSource("accountsProvider")
    void withdrawFromAccount(Account account, double amount) {
        var currentAmountOfTransactions = Bank.transactions.size();
        boolean result = false;

        if (amount > 0) {

            if (account instanceof CreditAccount) {

                if (amount <= ((CreditAccount) account).getCreditLimit() + account.getBalance()) {

                    result = Boolean.TRUE.equals(assertDoesNotThrow(
                            () -> Bank.withdraw(account.getAccountNumber(), amount),
                            "Should not throw exception when making a withdrawal from credit account"
                    ));

                    assertTrue(result, "Should withdraw money from account");
                    assertEquals(0d - amount, account.getBalance(), "Balance should be decreased by the amount of money withdrawn");
                    assertTrue(account.getBalance() + ((CreditAccount) account).getCreditLimit() >= 0, "Credit limit should not be exceeded");

                } else {
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> Bank.withdraw(account.getAccountNumber(), amount),
                            "Should throw exception when amount exceeds credit limit"
                    );

                    assertEquals(0d, account.getBalance(), "Balance should not be changed when amount exceeds credit limit");
                }

            } else if (account instanceof DebitAccount) {

                account.deposit(amount);

                result = Boolean.TRUE.equals(assertDoesNotThrow(
                        () -> Bank.withdraw(account.getAccountNumber(), amount),
                        "Should not throw exception when making a withdrawal from debit account"
                ));

                assertTrue(result, "Should withdraw money from account");
                assertEquals(0d, account.getBalance(), "Balance should be decreased by the amount of money withdrawn");

            }

        } else {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Bank.withdraw(account.getAccountNumber(), amount),
                    "Should throw exception when amount is less than or equal to 0"
            );
            assertEquals(0d, account.getBalance(), "Balance should not be changed when amount is less than or equal to 0");
        }

        assertEquals(currentAmountOfTransactions + 1, Bank.transactions.size(), "Should increase amount of transactions by 1");

        var transaction = Bank.transactions.getLast();

        assertEquals(TransactionType.WITHDRAWAL, transaction.getType(), "Transaction type should be WITHDRAWAL");
        assertEquals(amount, transaction.getAmount(), "Transaction should have correct amount of money");
        assertEquals(account.getAccountNumber(), transaction.getFromAccountNumber(), "Transaction should have correct from account number");
        assertEquals(result, transaction.isSuccess(), "Transaction should have correct success status");
        assertTrue(result ? Transaction.TRANSACTION_SUCCESS_MESSAGE.equals(transaction.getMessage()) : !transaction.getMessage().isEmpty(), "Transaction should have correct message");
    }

    private static Stream<Arguments> accountsProvider() {
        return Stream.of(
                Arguments.of(Bank.openDebitAccount(customer), 100d),
                Arguments.of(Bank.openCreditAccount(customer, 999d), 480d),
                Arguments.of(Bank.openDebitAccount(customer), -100d),
                Arguments.of(Bank.openCreditAccount(customer, 200d), 0d)
        );
    }

    @DisplayName("Should transfer money between accounts")
    @ParameterizedTest(name = "Should handle transfer of {0}")
    @ValueSource(doubles = {100d, 200d, -300d})
    void transferMoney(double amount) {
        var currentAmountOfTransactions = Bank.transactions.size();
        boolean result = false;

        var fromAccount = Bank.openCreditAccount(customer, 100d);
        var toAccount = Bank.openDebitAccount(customer);

        if (amount > 0) {

            if (fromAccount instanceof CreditAccount) {

                if (amount <= ((CreditAccount) fromAccount).getCreditLimit() + fromAccount.getBalance()) {

                    result = Boolean.TRUE.equals(assertDoesNotThrow(
                            () -> Bank.transfer(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), amount),
                            "Should not throw exception when making a transfer from credit account"
                    ));

                    assertTrue(result, "Should transfer money between accounts");
                    assertEquals(0d - amount, fromAccount.getBalance(), "Balance should be decreased by the transferred amount of money");
                    assertEquals(amount, toAccount.getBalance(), "Balance should be increased by the transferred amount of money");
                    assertTrue(
                            fromAccount.getBalance() + ((CreditAccount) fromAccount).getCreditLimit() >= 0,
                            "Credit limit should not be exceeded"
                    );

                } else {
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> Bank.transfer(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), amount),
                            "Should throw exception when amount exceeds credit limit"
                    );

                    assertEquals(0d, fromAccount.getBalance(), "Balance should not be changed when amount exceeds credit limit");
                    assertEquals(0d, toAccount.getBalance(), "Balance should not be changed when amount exceeds credit limit");
                }

            } else if (fromAccount instanceof DebitAccount) {

                fromAccount.deposit(amount);

                result = Boolean.TRUE.equals(assertDoesNotThrow(
                        () -> Bank.transfer(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), amount),
                        "Should not throw exception when making a transfer from debit account"
                ));

                assertTrue(result, "Should withdraw money from account");
                assertEquals(0d, fromAccount.getBalance(), "Balance should be decreased by the transferred amount of money");
                assertEquals(amount, toAccount.getBalance(), "Balance should be increased by the transferred amount of money");

            }

        } else {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Bank.transfer(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), amount),
                    "Should throw exception when amount is less than or equal to 0"
            );

            assertEquals(0d, fromAccount.getBalance(), "Balance should not be changed when amount is less than or equal to 0");
            assertEquals(0d, toAccount.getBalance(), "Balance should not be changed when amount is less than or equal to 0");
        }

        assertEquals(currentAmountOfTransactions + 1, Bank.transactions.size(), "Should increase amount of transactions by 1");

        var transaction = Bank.transactions.getLast();

        assertEquals(TransactionType.TRANSFER, transaction.getType(), "Transaction type should be TRANSFER");
        assertEquals(amount, transaction.getAmount(), "Transaction should have correct amount of money");
        assertEquals(fromAccount.getAccountNumber(), transaction.getFromAccountNumber(), "Transaction should have correct from account number");
        assertEquals(toAccount.getAccountNumber(), transaction.getToAccountNumber(), "Transaction should have correct to account number");
        assertEquals(result, transaction.isSuccess(), "Transaction should have correct success status");
        assertTrue(result ? Transaction.TRANSACTION_SUCCESS_MESSAGE.equals(transaction.getMessage()) : !transaction.getMessage().isEmpty(), "Transaction should have correct message");
    }

}
