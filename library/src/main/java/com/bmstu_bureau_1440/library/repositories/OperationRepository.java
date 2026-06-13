package com.bmstu_bureau_1440.library.repositories;

import java.util.List;

import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bmstu_bureau_1440.library.models.Book;
import com.bmstu_bureau_1440.library.models.Client;
import com.bmstu_bureau_1440.library.models.Operation;

public interface OperationRepository extends CrudRepository<Operation, Long> {
    List<Operation> findByBook(AggregateReference<Book, Long> book);

    @Query("SELECT books.* FROM operations JOIN books ON operations.book_id = books.id WHERE operations.client_id = :clientId GROUP BY books.id HAVING MOD(COUNT(*), 2) = 1;")
    List<Book> findWithdrawnBooksByClient(@Param("clientId") AggregateReference<Client, Long> client);
}
