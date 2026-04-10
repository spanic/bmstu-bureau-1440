package com.bmstu_bureau_1440.accounting.io;

import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.terminal.Frame;
import dev.tamboui.text.CharWidth;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.Size;
import dev.tamboui.toolkit.element.StyledElement;
import dev.tamboui.widgets.table.Cell;
import dev.tamboui.widgets.table.Row;
import dev.tamboui.widgets.table.Table;
import dev.tamboui.widgets.table.TableState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.bmstu_bureau_1440.accounting.io.utils.TuiUtils.truncateRow;

public abstract class AbstractTableWidget<T, K> extends StyledElement<AbstractTableWidget<T, K>> {

    protected final K controller;

    protected record Column<T>(String name, Constraint constraint, Function<T, String> valueExtractor) {
    }

    protected List<Column<T>> columns;

    protected TableState tableState;

    public AbstractTableWidget(K controller) {
        this.controller = controller;
        this.columns = getColumns();
        this.tableState = getStateProvider().apply(controller);
    }

    protected abstract Function<K, TableState> getStateProvider();

    protected abstract List<Column<T>> getColumns();

    protected abstract Function<K, List<T>> getDataProvider();

    @Override
    protected void renderContent(Frame frame, Rect rect, RenderContext renderContext) {
        List<T> data = getDataProvider().apply(controller);

        Row header = Row.from(columns.stream().map(Column::name).map(Cell::from).toArray(Cell[]::new))
                .style(Style.EMPTY.bold().fg(Color.YELLOW));

        final Constraint[] constraints = columns.stream().map(Column::constraint).toArray(Constraint[]::new);

        List<Row> rows = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            var object = data.get(i);
            final var values = columns.stream().map(column -> column.valueExtractor.apply(object)).toArray(String[]::new);
            final String[] truncatedValues = truncateRow(values, constraints, rect.width(), 4, 1, CharWidth.TruncatePosition.MIDDLE);

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

        frame.renderStatefulWidget(table, rect, tableState);
    }

    @Override
    public Size preferredSize(int availableWidth, int availableHeight, RenderContext context) {
        int rowCount = getDataProvider().apply(controller).size();
        int height = rowCount + 1 /* Header row */;
        if (availableHeight > 0) {
            height = Math.min(height, availableHeight);
        }

        return Size.heightOnly(height);
    }

}
