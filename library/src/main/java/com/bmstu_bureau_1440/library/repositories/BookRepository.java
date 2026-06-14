package com.bmstu_bureau_1440.library.repositories;

import java.util.List;

import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bmstu_bureau_1440.library.models.Book;
import com.bmstu_bureau_1440.library.models.Client;
import com.bmstu_bureau_1440.library.models.TrendingBook;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAvailableTrue();

    @Query("""
            SELECT books.*
            FROM operations
            JOIN books ON operations.book_id = books.id WHERE operations.client_id = :clientId
            GROUP BY books.id
            HAVING MOD(COUNT(*), 2) = 1;
            """)
    List<Book> findWithdrawnBooksByClient(@Param("clientId") AggregateReference<Client, Long> client);

    @Query("""
            SELECT books.*, COUNT(operations.id) AS total_withdraw_operations_count
            FROM books
            LEFT JOIN operations ON books.id = operations.book_id
                                 AND operations.type = 'WITHDRAW'
            GROUP BY books.id
            ORDER BY total_withdraw_operations_count DESC
            LIMIT 10
            """)
    List<TrendingBook> findTop10TrendingBooks();
}