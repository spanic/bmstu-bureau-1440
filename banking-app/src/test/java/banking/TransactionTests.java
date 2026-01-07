package banking;

import com.bmstu_bureau_1440.banking.Transaction;
import com.bmstu_bureau_1440.banking.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionTests {

    @ParameterizedTest
    @DisplayName("Should create transaction")
    @MethodSource("transactionsProvider")
    void shouldCreateTransaction(TransactionType type,
                                 double amount,
                                 String fromAccountNumber,
                                 String toAccountNumber,
                                 boolean success,
                                 String message) {

        var transaction = Transaction.builder()
                .type(type)
                .amount(amount)
                .fromAccountNumber(fromAccountNumber)
                .toAccountNumber(toAccountNumber)
                .success(success)
                .message(message)
                .build();

        assertNotNull(transaction, "Transaction should not be null");
        assertNotNull(transaction.getTimestamp(), "Timestamp should not be null");
    }

    private static Stream<Arguments> transactionsProvider() {
        return Stream.of(
                Arguments.of(TransactionType.DEPOSIT, 100d, null, UUID.randomUUID().toString(), true, Transaction.TRANSACTION_SUCCESS_MESSAGE),
                Arguments.of(TransactionType.WITHDRAWAL, 990d, UUID.randomUUID().toString(), null, false, "Something went wrong"),
                Arguments.of(TransactionType.TRANSFER, 341_359d, UUID.randomUUID().toString(), UUID.randomUUID().toString(), true, Transaction.TRANSACTION_SUCCESS_MESSAGE)
        );
    }

}
