package banking;

import com.bmstu_bureau_1440.banking.Customer;
import com.bmstu_bureau_1440.banking.DebitAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DebitAccountTests {

    private Customer customer;

    @BeforeEach
    void setup() {
        customer = new Customer("Andrei Sadulin");
    }

    @Test
    @DisplayName("Should create account")
    void createDebitAccount() {
        var account = new DebitAccount(customer);

        assertNotNull(account, "Account is null");
        assertNotNull(account.getOwner(), "Owner is null");
        assertEquals(0, account.getBalance(), "Initial balance is not zero");

        try {
            UUID.fromString(account.getAccountNumber());
        } catch (IllegalArgumentException e) {
            fail("Account number is not a valid UUID: " + account.getAccountNumber());
        }
    }

    @Test
    @DisplayName("Should throw exception when Customer is null")
    void createDebitAccountWithNullCustomer() {
        assertThrows(
                NullPointerException.class,
                () -> new DebitAccount(null),
                "Should throw exception when Customer is null"
        );
    }

    @ParameterizedTest(name = "Should handle deposit of {0}")
    @DisplayName("Should deposit money")
    @ValueSource(doubles = {30d, 0d, -1d})
    void depositMoney(double amount) {
        var account = new DebitAccount(customer);

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
    @MethodSource("withdrawalAmountsProvider")
    void withdrawMoney(double initialAmount, double withdrawAmount) {
        var account = new DebitAccount(customer);
        account.deposit(initialAmount);

        if (withdrawAmount > 0 && withdrawAmount <= initialAmount) {
            boolean success = Boolean.TRUE.equals(
                    assertDoesNotThrow(
                            () -> account.withdraw(withdrawAmount),
                            "Withdrawal should not throw exception"
                    )
            );
            assertTrue(success, "Withdrawal failed");
            assertEquals(initialAmount - withdrawAmount, account.getBalance(), "Balance is not equal to expected amount");
        } else {
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(withdrawAmount));
        }
    }

    private static Stream<Arguments> withdrawalAmountsProvider() {
        return Stream.of(
                Arguments.of(10d, 100d),
                Arguments.of(100d, 10d),
                Arguments.of(10d, -100d)
        );
    }

}
