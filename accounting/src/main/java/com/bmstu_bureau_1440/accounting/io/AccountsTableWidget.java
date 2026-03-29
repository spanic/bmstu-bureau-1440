package com.bmstu_bureau_1440.accounting.io;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.terminal.Frame;
import dev.tamboui.text.CharWidth;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.Size;
import dev.tamboui.toolkit.element.StyledElement;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widgets.table.Cell;
import dev.tamboui.widgets.table.Row;
import dev.tamboui.widgets.table.Table;
import dev.tamboui.widgets.table.TableState;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AccountsTableWidget extends StyledElement<AccountsTableWidget> {

    @Getter
    private final TableState accountsTableState = new TableState();

    record Column<T>(String name, Constraint constraint, Function<T, String> valueExtractor) {
    }

    private final List<Column<BankAccount>> columns = List.of(
            new Column<>("ID", Constraint.percentage(30), BankAccount::getId),
            new Column<>("Name", Constraint.fill(), BankAccount::getName),
            new Column<>("Balance (₽)", Constraint.percentage(20), account -> account.getBalance().toString())
    );

    private final Storage storage;

    public AccountsTableWidget(Storage storage) {
        this.storage = storage;
        accountsTableState.selectFirst();
    }

    @Override
    protected void renderContent(Frame frame, Rect rect, RenderContext renderContext) {
        var accounts = this.storage.getAccounts();

        Row header = Row.from(columns.stream()
                        .map(Column::name)
                        .map(Cell::from)
                        .toArray(Cell[]::new))
                .style(Style.EMPTY.bold().fg(Color.YELLOW));

        final Constraint[] constraints = columns.stream()
                .map(Column::constraint)
                .toArray(Constraint[]::new);

        List<Row> rows = new ArrayList<>();

        for (int i = 0; i < accounts.size(); i++) {
            var account = accounts.get(i);

            final var values = columns.stream()
                    .map(column -> column.valueExtractor.apply(account))
                    .toArray(String[]::new);

            final String[] truncatedValues = EllipsisHelper.truncateRow(
                    values,
                    constraints,
                    rect.width(),
                    4,
                    1,
                    CharWidth.TruncatePosition.MIDDLE
            );

            Row row = Row.from(truncatedValues).style(i % 2 == 0 ? Style.EMPTY : Style.EMPTY.bg(Color.indexed(236)));

            rows.add(row);
        }

        Table table = Table.builder()
                .header(header)
                .rows(rows)
                .widths(constraints)
                .highlightStyle(Style.EMPTY.bg(Color.BLUE).fg(Color.WHITE).bold())
                .highlightSymbol("▶ ")
                .build();

        frame.renderStatefulWidget(table, rect, accountsTableState);
    }

    @Override
    public Size preferredSize(int availableWidth, int availableHeight, RenderContext context) {
        int rowCount = storage.getAccounts().size();
        int height = rowCount + 1 /* Header row */;
        if (availableHeight > 0) {
            height = Math.min(height, availableHeight);
        }

        return Size.heightOnly(height);
    }

    @Override
    public EventResult handleKeyEvent(KeyEvent event, boolean focused) {
        if (!focused) {
            return EventResult.UNHANDLED;
        }

        if (event.isUp()) {
            accountsTableState.selectPrevious();
            return EventResult.HANDLED;
        } else if (event.isDown()) {
            accountsTableState.selectNext(storage.getAccounts().size());
            return EventResult.HANDLED;
        } else {
            return EventResult.UNHANDLED;
        }
    }

    public BankAccount getSelectedObject() {
        Integer index = accountsTableState.selected();
        if (index == null) {
            return null;
        }
        var accounts = storage.getAccounts();
        if (index >= 0 && index < accounts.size()) {
            return accounts.get(index);
        }
        return null;
    }
}
