package banking;

import com.bmstu_bureau_1440.banking.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTests {

    @ParameterizedTest(name = "Create customer with name {0}")
    @DisplayName("Should create customer")
    @ValueSource(strings = {"Andrei Sadulin"})
    void createCustomer(String name) {
        var customer = new Customer(name);

        assertNotNull(customer, "Customer is null");
        assertEquals(name, customer.getName(), "Name is not equal");

        try {
            UUID.fromString(customer.getId());
        } catch (IllegalArgumentException e) {
            fail("Customer ID is not a valid UUID: " + customer.getId());
        }
    }

    @Test
    @DisplayName("Should throw exception if name is null")
    void throwExceptionIfNameIsNull() {
        assertThrows(NullPointerException.class, () -> new Customer(null), "Name is null");
    }

}
