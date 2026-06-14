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

class BookRepositoryTest extends AbstractIntegrationTest {

        @Autowired
        private BookRepository bookRepository;

        @Autowired
        private OperationRepository operationRepository;

        @Autowired
        private ClientRepository clientRepository;

        @BeforeEach
        void setUp() {
                operationRepository.deleteAll();
                bookRepository.deleteAll();
                clientRepository.deleteAll();
        }

        @Test
        void saveAndFindById() {
                var book = new Book(null, "Eugene Onegin", "Alexander Pushkin", Genre.POETRY, true);
                var saved = bookRepository.save(book);

                var found = bookRepository.findById(saved.getId());

                assertThat(found).isPresent();
                assertThat(found.get().getTitle()).isEqualTo("Eugene Onegin");
                assertThat(found.get().getAuthor()).isEqualTo("Alexander Pushkin");
                assertThat(found.get().getGenre()).isEqualTo(Genre.POETRY);
                assertThat(found.get().isAvailable()).isTrue();
        }

        @Test
        void findAllReturnsAllBooks() {
                bookRepository.save(new Book(null, "Dead Souls", "Nikolai Gogol", Genre.NOVEL, true));
                bookRepository.save(new Book(null, "The Idiot", "Fyodor Dostoevsky", Genre.NOVEL, false));

                var all = bookRepository.findAll();

                assertThat(all).hasSize(2);
        }

        @Test
        void findByTitleContainingIgnoreCaseReturnsBooksIfTitleMatches() {
                bookRepository.save(new Book(null, "Мертвые души", "Николай Андреевич Гоголь", Genre.NOVEL, true));
                bookRepository.save(new Book(null, "Идиот", "Федор Михайлович Достоевский", Genre.NOVEL, false));
                bookRepository.save(
                                new Book(null, "Меркурий и Венера – таинственные планеты", "Иванов И. И.",
                                                Genre.SCIENCE_FICTION,
                                                true));

                var books = bookRepository.findByTitleContainingIgnoreCase("МЕР");

                assertThat(books).hasSize(2);
        }

        @Test
        void findWithdrawnBooksByClient() {
                var book1 = bookRepository.save(new Book(null, "Fathers and Sons", "Ivan Turgenev", Genre.NOVEL, true));
                var book2 = bookRepository.save(new Book(null, "War and Peace", "Leo Tolstoy", Genre.NOVEL, true));

                var client = clientRepository
                                .save(new Client(null, "Alexei Ivanov", "alexei.ivanov@example.com", null));

                operationRepository.save(new Operation(null, AggregateReference.to(client.getId()),
                                AggregateReference.to(book1.getId()), OperationType.WITHDRAW, null));
                operationRepository.save(new Operation(null, AggregateReference.to(client.getId()),
                                AggregateReference.to(book2.getId()), OperationType.WITHDRAW, null));

                var withdrawnBooks = bookRepository.findWithdrawnBooksByClient(AggregateReference.to(client.getId()));

                assertThat(withdrawnBooks).hasSize(2);
                assertThat(withdrawnBooks).contains(book1, book2);

                operationRepository.save(new Operation(null, AggregateReference.to(client.getId()),
                                AggregateReference.to(book1.getId()), OperationType.RETURN, null));

                withdrawnBooks = bookRepository.findWithdrawnBooksByClient(AggregateReference.to(client.getId()));

                assertThat(withdrawnBooks).hasSize(1);
                assertThat(withdrawnBooks).first().isEqualTo(book2);

        }

        @Test
        void findTop10TrendingBooks() {
                var book1 = bookRepository.save(new Book(null, "Fathers and Sons", "Ivan Turgenev", Genre.NOVEL, true));
                var book2 = bookRepository.save(new Book(null, "War and Peace", "Leo Tolstoy", Genre.NOVEL, true));
                var book3 = bookRepository
                                .save(new Book(null, "The Master and Margarita", "Mikhail Bulgakov", Genre.FANTASY,
                                                true));

                var client = clientRepository
                                .save(new Client(null, "Alexei Ivanov", "alexei.ivanov@example.com", null));

                operationRepository.save(new Operation(null, AggregateReference.to(client.getId()),
                                AggregateReference.to(book1.getId()), OperationType.WITHDRAW, null));

                operationRepository.save(new Operation(null, AggregateReference.to(client.getId()),
                                AggregateReference.to(book1.getId()), OperationType.WITHDRAW, null));

                operationRepository.save(new Operation(null, AggregateReference.to(client.getId()),
                                AggregateReference.to(book2.getId()), OperationType.WITHDRAW, null));

                var trendingBooks = bookRepository.findTop10TrendingBooks();

                assertThat(trendingBooks).hasSize(3);
                assertThat(trendingBooks).first().matches(
                                book -> book.getId().equals(book1.getId())
                                                && book.getTotalWithdrawOperationsCount().equals(2L));
                assertThat(trendingBooks.get(1)).matches(
                                book -> book.getId().equals(book2.getId())
                                                && book.getTotalWithdrawOperationsCount().equals(1L));
                assertThat(trendingBooks).last().matches(
                                book -> book.getId().equals(book3.getId())
                                                && book.getTotalWithdrawOperationsCount().equals(0L));
        }

}
