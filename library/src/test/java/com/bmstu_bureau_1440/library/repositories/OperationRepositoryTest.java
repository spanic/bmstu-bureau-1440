package com.bmstu_bureau_1440.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import com.bmstu_bureau_1440.library.AbstractIntegrationTest;
import com.bmstu_bureau_1440.library.models.Book;
import com.bmstu_bureau_1440.library.models.Client;
import com.bmstu_bureau_1440.library.models.Genre;
import com.bmstu_bureau_1440.library.models.Operation;
import com.bmstu_bureau_1440.library.models.OperationType;

class OperationRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OperationRepository operationRepository;

    @BeforeEach
    void setUp() {
        operationRepository.deleteAll();
        bookRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void saveAndFindByBookId() {
        var book = bookRepository.save(new Book(null, "Fathers and Sons", "Ivan Turgenev", Genre.NOVEL, true));
        var client = clientRepository.save(new Client(null, "Alexei Ivanov", "alexei.ivanov@example.com", null));

        operationRepository.save(
                new Operation(null, AggregateReference.to(client.getId()), AggregateReference.to(book.getId()),
                        OperationType.WITHDRAW, null));

        var operations = operationRepository.findByBook(AggregateReference.to(book.getId()));

        assertThat(operations).hasSize(1);
        assertThat(operations.getFirst().getType()).isEqualTo(OperationType.WITHDRAW);
        assertThat(operations.getFirst().getBook().getId()).isEqualTo(book.getId());
        assertThat(operations.getFirst().getClient().getId()).isEqualTo(client.getId());
        assertThat(operations.getFirst().getPerformedAt()).isNotNull();
    }

    @Test
    void findWithdrawnBooksByClient() {
        var book1 = bookRepository.save(new Book(null, "Fathers and Sons", "Ivan Turgenev", Genre.NOVEL, true));
        var book2 = bookRepository.save(new Book(null, "War and Peace", "Leo Tolstoy", Genre.NOVEL, true));

        var client = clientRepository.save(new Client(null, "Alexei Ivanov", "alexei.ivanov@example.com", null));

        operationRepository.save(new Operation(null, AggregateReference.to(client.getId()),
                AggregateReference.to(book1.getId()), OperationType.WITHDRAW, null));
        operationRepository.save(new Operation(null, AggregateReference.to(client.getId()),
                AggregateReference.to(book2.getId()), OperationType.WITHDRAW, null));

        var withdrawnBooks = operationRepository.findWithdrawnBooksByClient(AggregateReference.to(client.getId()));

        assertThat(withdrawnBooks).hasSize(2);
        assertThat(withdrawnBooks).contains(book1, book2);

        operationRepository.save(new Operation(null, AggregateReference.to(client.getId()),
                AggregateReference.to(book1.getId()), OperationType.RETURN, null));

        withdrawnBooks = operationRepository.findWithdrawnBooksByClient(AggregateReference.to(client.getId()));

        assertThat(withdrawnBooks).hasSize(1);
        assertThat(withdrawnBooks).first().isEqualTo(book2);

    }
}
