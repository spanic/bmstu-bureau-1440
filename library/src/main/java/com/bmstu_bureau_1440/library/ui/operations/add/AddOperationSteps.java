package com.bmstu_bureau_1440.library.ui.operations.add;

import java.util.stream.StreamSupport;

import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.transaction.annotation.Transactional;

import com.bmstu_bureau_1440.library.models.Book;
import com.bmstu_bureau_1440.library.models.Client;
import com.bmstu_bureau_1440.library.models.OperationType;
import com.bmstu_bureau_1440.library.ui.models.ParametrizedStepExecutor;
import com.bmstu_bureau_1440.shared.io.IO;

public class AddOperationSteps {

    public static class ChooseClientStep implements ParametrizedStepExecutor<AddOperationContext> {

        @Override
        public AddOperationOrchestrator.Steps execute(
                AddOperationContext context) {

            Client[] clients = StreamSupport.stream(context.getClientRepository().findAll().spliterator(), false)
                    .toArray(Client[]::new);

            var selectedClient = IO.inputWithAutocomplete("Выберите читателя:", clients, Client::toString);

            if (selectedClient == null) {
                IO.displayError(new Exception("Читатель не выбран"));
                return null;
            } else {
                context.getOperation().setClient(AggregateReference.to(selectedClient.getId()));
                return AddOperationOrchestrator.Steps.BOOK;

            }

        }

    }

    public static class ChooseBookStep implements ParametrizedStepExecutor<AddOperationContext> {

        @Override
        public AddOperationOrchestrator.Steps execute(AddOperationContext context) {

            Book[] books = context.getBookRepository().findByAvailableTrue().toArray(Book[]::new);

            var selectedBook = IO.inputWithAutocomplete("Выберите книгу:", books, Book::toString);

            if (selectedBook == null) {
                IO.displayError(new Exception("Книга не выбрана"));
                return null;
            } else {
                context.getOperation().setBook(AggregateReference.to(selectedBook.getId()));
                context.setBook(selectedBook);
                return AddOperationOrchestrator.Steps.CONFIRMATION;
            }

        }

    }

    public static class ConfirmationStep implements ParametrizedStepExecutor<AddOperationContext> {

        @Override
        @Transactional
        public AddOperationOrchestrator.Steps execute(AddOperationContext context) {
            var book = context.getBook();
            book.setAvailable(false);
            context.getBookRepository().save(book);

            context.getOperation().setType(OperationType.WITHDRAW);
            var savedOperation = context.getOperationRepository().save(context.getOperation());

            IO.displaySuccess("Операция успешно добавлена: " + savedOperation.getId());

            return null;
        }

    }

}
