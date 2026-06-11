package com.bmstu_bureau_1440.library.ui.books.find;

import com.bmstu_bureau_1440.library.ui.books.find.FindBookOperationOrchestrator.FindBookSteps;
import com.bmstu_bureau_1440.library.ui.models.ParametrizedStepExecutor;
import com.bmstu_bureau_1440.shared.io.IO;

public class FindBookOperationSteps {

    public static class EnterBookTitleStep implements ParametrizedStepExecutor<FindBookOperationContext> {

        @Override
        public FindBookSteps execute(FindBookOperationContext context) {
            context.title = (IO.inputString("Введите название книги:"));
            return FindBookSteps.VIEW_RESULTS;
        }

    }

    public static class ViewResultsStep implements ParametrizedStepExecutor<FindBookOperationContext> {

        @Override
        public FindBookSteps execute(FindBookOperationContext context) {
            var books = context.getBookRepository().findByTitleContainingIgnoreCase(context.title);

            if (books.isEmpty()) {
                IO.displayWarning(String.format("Книга, где в названии есть \"%s\", не найдена", context.title));
                return null;
            } else {
                IO.displaySuccess(String.format("Найдено книг – %d шт.:", books.size()));
                books.forEach(book -> System.out.println(book.toString()));
            }
            return null;
        }

    }

}
