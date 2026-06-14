package com.bmstu_bureau_1440.library.ui.books.add;

import com.bmstu_bureau_1440.library.models.Genre;
import com.bmstu_bureau_1440.library.ui.books.add.AddBookOperationOrchestrator.AddBookSteps;
import com.bmstu_bureau_1440.library.ui.models.ParametrizedStepExecutor;
import com.bmstu_bureau_1440.shared.io.IO;

public class AddBookOperationSteps {

    public static class EnterBookTitleStep implements ParametrizedStepExecutor<AddBookOperationContext> {

        @Override
        public AddBookSteps execute(AddBookOperationContext context) {
            context.getBook().setTitle(IO.inputString("Как называется книга?"));
            return AddBookSteps.AUTHOR;
        }

    }

    public static class EnterBookAuthorStep implements ParametrizedStepExecutor<AddBookOperationContext> {

        @Override
        public AddBookSteps execute(AddBookOperationContext context) {
            context.getBook().setAuthor(IO.inputString("Кто автор?"));
            return AddBookSteps.GENRE;
        }

    }

    public static class SelectBookGenreStep implements ParametrizedStepExecutor<AddBookOperationContext> {

        @Override
        public AddBookSteps execute(AddBookOperationContext context) {
            context.getBook().setGenre(IO.inputListOptions("Какой жанр?", Genre.values()));
            return AddBookSteps.CONFIRMATION;
        }

    }

    public static class AddBookConfirmationStep implements ParametrizedStepExecutor<AddBookOperationContext> {

        @Override
        public AddBookSteps execute(AddBookOperationContext context) {
            var savedBook = context.getBookRepository().save(context.getBook());
            IO.displaySuccess("Книга успешно добавлена: " + savedBook.getId());
            return null;
        }

    }

}
