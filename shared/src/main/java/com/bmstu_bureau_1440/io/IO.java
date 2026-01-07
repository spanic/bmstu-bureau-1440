package com.bmstu_bureau_1440.io;

import org.jline.consoleui.prompt.ConsolePrompt;
import org.jline.consoleui.prompt.PromptResultItemIF;
import org.jline.consoleui.prompt.builder.ListPromptBuilder;
import org.jline.consoleui.prompt.builder.PromptBuilder;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class IO {

    public static Terminal terminal;

    static {
        try {
            terminal = TerminalBuilder.builder().system(true).build();
        } catch (IOException e) {
            System.err.println("Error while creating terminal: " + e.getMessage());
        }
    }

    public static String displayMenu(IOperation... operations) {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        ListPromptBuilder builder = prompt.getPromptBuilder().createListPrompt();

        builder.name("operation").message("Choose operation:");

        for (IOperation operation : operations) {
            AttributedStringBuilder textBuilder = new AttributedStringBuilder();
            // TIP: it's important to set style before text while using AttributedStringBuilder
            if (operation.getStyle() != null) {
                textBuilder = textBuilder.style(operation.getStyle());
            }
            textBuilder.append(operation.getText());

            builder.newItem(operation.getOperation()).text(textBuilder.toAttributedString().toAnsi()).add();
        }

        try {
            Map<String, PromptResultItemIF> result = prompt.prompt(builder.addPrompt().build());
            return result.get("operation").getResult();
        } catch (IOException e) {
            System.err.println("Error while getting operation from menu: " + e.getMessage());
        }

        return Operation.EXIT.getOperation();
    }

    public static String inputString(String label) {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();

        builder.createInputPrompt()
                .name("input")
                .message(label)
                .addPrompt();

        try {
            Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
            var output = result.get("input").getResult();
            return Objects.equals(output, "null") ? null : output;
        } catch (Exception e) {
            System.err.println("Error while getting info from input prompt: " + e.getMessage());
        }

        return null;
    }

    public static void displaySuccess(String message) {
        if (message != null && !message.isBlank()) {
            AttributedString greenText =
                    new AttributedString(message, AttributedStyle.BOLD.foreground(AttributedStyle.GREEN));
            terminal.writer().println(greenText.toAnsi(terminal));
        }
    }

    public static void displayWarning(String message) {
        if (message != null && !message.isBlank()) {
            AttributedString orangeText =
                    new AttributedString(message, AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW));
            terminal.writer().println(orangeText.toAnsi(terminal));
        }
    }

    public static void displayError(Exception e) {
        AttributedString redText =
                new AttributedString("Error: " + e.getMessage(), AttributedStyle.BOLD.foreground(AttributedStyle.RED));
        terminal.writer().println(redText.toAnsi(terminal));
    }

    public static <T> String inputWithAutocomplete(String label,
                                                   T[] entities,
                                                   Function<T, String> getSearchValueFn,
                                                   Function<T, String> getSuggestionFn) {

        var colouredLabel = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                .append("?")
                .style(AttributedStyle.BOLD)
                .append(" ")
                .append(label)
                .toAttributedString()
                .toAnsi();

        List<Candidate> candidates = new ArrayList<>();

        for (T entity : entities) {
            var suggestion = getSuggestionFn.apply(entity);
            var searchValue = getSearchValueFn.apply(entity);

            candidates.add(new Candidate(searchValue, searchValue, null, suggestion, null, null, true));
        }

        Completer completer = (reader, line, completions) ->
                completions.addAll(candidates);

        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .option(LineReader.Option.LIST_PACKED, true)
                .build();

        try {
            String output = reader.readLine(colouredLabel).trim();
            return Objects.equals(output, "null") ? null : output;
        } catch (Exception e) {
            System.err.println("Error while getting info from input prompt: " + e.getMessage());
        }

        return null;
    }

}