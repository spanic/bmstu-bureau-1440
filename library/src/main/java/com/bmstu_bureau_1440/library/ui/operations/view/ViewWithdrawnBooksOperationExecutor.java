package com.bmstu_bureau_1440.library.ui.operations.view;

import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.library.models.Client;
import com.bmstu_bureau_1440.library.repositories.BookRepository;
import com.bmstu_bureau_1440.library.repositories.ClientRepository;
import com.bmstu_bureau_1440.shared.io.IO;

@Component
public class ViewWithdrawnBooksOperationExecutor implements Runnable {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void run() {

        Client[] clients = StreamSupport.stream(clientRepository.findAll().spliterator(), false)
                .toArray(Client[]::new);

        var selectedClient = IO.inputWithAutocomplete("Выберите читателя:", clients, Client::toString);

        if (selectedClient == null) {
            IO.displayError(new Exception("Читатель не выбран"));
            return;
        }

        var withdrawnBooks = bookRepository
                .findWithdrawnBooksByClient(AggregateReference.to(selectedClient.getId()));

        if (withdrawnBooks.isEmpty()) {
            IO.displayWarning("Книг не найдено");
            return;
        } else {
            IO.displaySuccess(String.format("Найдено книг – %d шт.:", withdrawnBooks.size()));
        }

        withdrawnBooks.forEach(book -> System.out.println(book.toString()));

    }

}
