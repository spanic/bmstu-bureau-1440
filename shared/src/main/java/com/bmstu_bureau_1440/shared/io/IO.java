package com.bmstu_bureau_1440.shared.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jline.consoleui.prompt.ConsolePrompt;
import org.jline.consoleui.prompt.PromptResultItemIF;
import org.jline.consoleui.prompt.builder.ListPromptBuilder;
import org.jline.consoleui.prompt.builder.PromptBuilder;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class IO {

    public static Terminal terminal;

    static {
        try {
            terminal = TerminalBuilder.builder().system(true).build();
        } catch (IOException e) {
            System.err.println("Error while creating terminal: " + e.getMessage());
        }
    }

    public static <T extends ListOption> T inputListOptions(String label, T[] entities) {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        ListPromptBuilder builder = prompt.getPromptBuilder().createListPrompt();

        builder.name("list").message(label);

        for (T entity : entities) {
            AttributedStringBuilder textBuilder = new AttributedStringBuilder();
            if (entity.getStyle() != null) {
                textBuilder = textBuilder.style(entity.getStyle());
            }
            textBuilder.append(entity.getName());

            builder.newItem(entity.getKey()).text(textBuilder.toAttributedString().toAnsi()).add();
        }

        String result;
        try {
            result = prompt.prompt(builder.addPrompt().build()).get("list").getResult();
            return Stream.of(entities)
                    .filter(e -> e.getKey().equals(result))
                    .findFirst()
                    .orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException("Error while getting list options: " + e.getMessage());
        }
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
            AttributedString greenText = new AttributedString(message,
                    AttributedStyle.BOLD.foreground(AttributedStyle.GREEN));
            terminal.writer().println(greenText.toAnsi(terminal));
            terminal.flush();
        }
    }

    public static void displayWarning(String message) {
        if (message != null && !message.isBlank()) {
            AttributedString orangeText = new AttributedString(message,
                    AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW));
            terminal.writer().println(orangeText.toAnsi(terminal));
            terminal.flush();
        }
    }

    public static void displayError(Exception e) {
        AttributedString redText = new AttributedString("Error: " + e.getMessage(),
                AttributedStyle.BOLD.foreground(AttributedStyle.RED));
        terminal.writer().println(redText.toAnsi(terminal));
        terminal.flush();
    }

    public static <T> T inputWithAutocomplete(String label,
            T[] entities,
            Function<T, String> getSuggestionFn) {
        return inputWithAutocomplete(label, entities, getSuggestionFn, (a) -> null);
    }

    public static <T> T inputWithAutocomplete(String label,
            T[] entities,
            Function<T, String> getKeyFn,
            Function<T, String> getSuggestionFn) {

        var colouredLabel = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                .append("?")
                .style(AttributedStyle.BOLD)
                .append(" ")
                .append(label)
                .append(" ")
                .toAttributedString()
                .toAnsi();

        List<Candidate> candidates = new ArrayList<>();

        for (T entity : entities) {
            var suggestion = getSuggestionFn.apply(entity);
            var searchValue = getKeyFn.apply(entity);

            candidates.add(new Candidate(searchValue, searchValue, null, suggestion,
                    null, null, false));
        }

        Completer completer = (reader, line, completions) -> completions.addAll(candidates);

        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(new char[0]);

        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .parser(parser)
                .option(LineReader.Option.LIST_PACKED, true)
                .build();

        try {
            String output = reader.readLine(colouredLabel).trim();
            return Objects.equals(output, "null") ? null
                    : Stream.of(entities).filter(e -> getKeyFn.apply(e).equals(output)).findFirst().orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Error while getting info from input prompt: " + e.getMessage());
        }

    }

}