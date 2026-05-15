package com.bmstu_bureau_1440.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bmstu_bureau_1440.library.AbstractIntegrationTest;
import com.bmstu_bureau_1440.library.models.Book;
import com.bmstu_bureau_1440.library.models.Operation;
import com.bmstu_bureau_1440.library.models.OperationType;

class OperationRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OperationRepository operationRepository;

    @BeforeEach
    void setUp() {
        operationRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    void saveAndFindByBookId() {
        var book = bookRepository.save(
                new Book(null, "Fathers and Sons", "Ivan Turgenev", "978-0-14-044747-7", true));

        operationRepository.save(new Operation(
                null, book.getId(), OperationType.WITHDRAW, LocalDateTime.now(), "Alexei Ivanov"));

        var operations = operationRepository.findByBookId(book.getId());

        assertThat(operations).hasSize(1);
        assertThat(operations.getFirst().getType()).isEqualTo(OperationType.WITHDRAW);
        assertThat(operations.getFirst().getBorrowerName()).isEqualTo("Alexei Ivanov");
    }
}
