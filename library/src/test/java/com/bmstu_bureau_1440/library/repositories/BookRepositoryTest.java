package com.bmstu_bureau_1440.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bmstu_bureau_1440.library.AbstractIntegrationTest;
import com.bmstu_bureau_1440.library.models.Book;

class BookRepositoryTest extends AbstractIntegrationTest {

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
    void saveAndFindById() {
        var book = new Book(null, "Eugene Onegin", "Alexander Pushkin", "978-0-14-044894-8", true);
        var saved = bookRepository.save(book);

        var found = bookRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Eugene Onegin");
        assertThat(found.get().getAuthor()).isEqualTo("Alexander Pushkin");
        assertThat(found.get().isAvailable()).isTrue();
    }

    @Test
    void findAllReturnsAllBooks() {
        bookRepository.save(new Book(null, "Dead Souls", "Nikolai Gogol", "978-0-14-044807-8", true));
        bookRepository.save(new Book(null, "The Idiot", "Fyodor Dostoevsky", "978-0-14-044792-7", false));

        var all = bookRepository.findAll();

        assertThat(all).hasSize(2);
    }
}
