package banking;

import com.bmstu_bureau_1440.banking.CreditAccount;
import com.bmstu_bureau_1440.banking.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreditAccountTests {

    private Customer customer;

    @BeforeEach
    void setup() {
        customer = new Customer("Andrei Sadulin");
    }

    @ParameterizedTest(name = "Should create account with credit limit of {0}")
    @DisplayName("Should create account")
    @ValueSource(doubles = {100_000d, 0d, -1d})
    void createCreditAccount(double limit) {
        if (limit < 0) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new CreditAccount(customer, limit),
                    "Should throw exception when credit limit is negative"
            );
            return;
        }

        var account = new CreditAccount(customer, limit);

        assertNotNull(account, "Account is null");
        assertNotNull(account.getOwner(), "Owner is null");
        assertEquals(0, account.getBalance(), "Initial balance is not zero");
        assertEquals(limit, account.getCreditLimit(), "Credit limit is not equal to expected limit");

        try {
            UUID.fromString(account.getAccountNumber());
        } catch (IllegalArgumentException e) {
            fail("Account number is not a valid UUID: " + account.getAccountNumber());
        }
    }

    @Test
    @DisplayName("Should throw exception when Customer is null")
    void createCreditAccountWithNullCustomer() {
        assertThrows(
                NullPointerException.class,
                () -> new CreditAccount(null, 0d),
                "Should throw exception when Customer is null"
        );
    }

    @ParameterizedTest(name = "Should handle deposit of {0}")
    @DisplayName("Should deposit money")
    @ValueSource(doubles = {30d, 0d, -1d})
    void depositMoney(double amount) {
        var account = new CreditAccount(customer, 100d);

        if (amount > 0) {
            boolean success = Boolean.TRUE.equals(
                    assertDoesNotThrow(
                            () -> account.deposit(amount),
                            "Deposit should not throw exception")
            );
            assertTrue(success, "Deposit failed");
            assertEquals(amount, account.getBalance(), "Balance is not equal to expected deposit amount");
        } else {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> account.deposit(amount),
                    "Deposit insufficient amount should throw exception"
            );
        }
    }

    @ParameterizedTest(name = "Should handle withdrawal of {1} from {0}")
    @DisplayName("Should withdraw money")
    @ValueSource(doubles = {30d, 200d, -1d})
    void withdrawMoney(double withdrawAmount) {
        var account = new CreditAccount(customer, 100d);

        if (withdrawAmount > 0 && withdrawAmount <= account.getBalance() + account.getCreditLimit()) {
            boolean success = Boolean.TRUE.equals(
                    assertDoesNotThrow(
                            () -> account.withdraw(withdrawAmount),
                            "Withdrawal should not throw exception"
                    )
            );
            assertTrue(success, "Withdrawal failed");
            assertEquals(0 - withdrawAmount, account.getBalance(), "Balance is not equal to expected amount");
        } else {
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(withdrawAmount));
        }
    }

}
