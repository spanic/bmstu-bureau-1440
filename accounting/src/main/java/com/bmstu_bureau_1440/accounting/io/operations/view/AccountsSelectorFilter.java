package com.bmstu_bureau_1440.accounting.io.operations.view;

import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.terminal.Frame;
import dev.tamboui.text.MarkupParser;
import dev.tamboui.text.Text;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.Size;
import dev.tamboui.toolkit.element.StyledElement;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyCode;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widgets.list.ListItem;
import dev.tamboui.widgets.list.ListState;
import dev.tamboui.widgets.list.ListWidget;

import java.util.ArrayList;
import java.util.List;

public class AccountsSelectorFilter extends StyledElement<AccountsSelectorFilter> {

    private enum Status {
        SELECTED,
        NOT_SELECTED
    }

    private static class Item {
        final String title;
        final String info;
        Status status;

        Item(Status status, String todo, String info) {
            this.status = status;
            this.title = todo;
            this.info = info;
        }
    }

    private final ListState listState = new ListState();

    private final List<Item> items = List.of(
            new Item(Status.NOT_SELECTED, "test", "123"),
            new Item(Status.NOT_SELECTED, "hello", "123")
    );

    @Override
    protected void renderContent(Frame frame, Rect area, RenderContext context) {
        renderList(frame, area);
    }

    private List<ListItem> buildListItems() {
        List<ListItem> items = new ArrayList<>();
        for (int i = 0; i < this.items.size(); i++) {
            Item todoItem = this.items.get(i);
            Color bgColor = (i % 2 == 0)
                    ? Color.indexed(235)  // Dark gray (similar to SLATE.c950)
                    : Color.indexed(236); // Slightly lighter (similar to SLATE.c900)

            Text line;
            if (todoItem.status == Status.NOT_SELECTED) {
                line = MarkupParser.parse("[light-gray] :black_square_button: " + todoItem.title);
            } else {
                line = MarkupParser.parse("[green] :white_check_mark: " + todoItem.title);
            }

            ListItem item = ListItem.from(line).style(Style.EMPTY.bg(bgColor));
            items.add(item);
        }
        return items;
    }

    private void renderList(Frame frame, Rect area) {
        var items = buildListItems().stream()
                .map(ListItem::toSizedWidget)
                .toList();

        ListWidget list = ListWidget.builder()
                .items(items)
                .style(Style.EMPTY.fg(Color.indexed(250)))
                .highlightStyle(Style.EMPTY.bg(Color.indexed(238)).bold())
                .highlightSymbol(">")
                .build();

        frame.renderStatefulWidget(list, area, listState);
    }

    private void toggleStatus() {
        Integer selected = listState.selected();
        if (selected != null && selected >= 0 && selected < items.size()) {
            Item item = items.get(selected);
            item.status = item.status == Status.NOT_SELECTED ? Status.SELECTED : Status.NOT_SELECTED;
        }
    }

    @Override
    public EventResult handleKeyEvent(KeyEvent event, boolean focused) {
        if (!focused) {
            return EventResult.UNHANDLED;
        }

        if (event.isUp()) {
            listState.selectPrevious();
            return EventResult.HANDLED;
        } else if (event.isDown()) {
            listState.selectNext(items.size());
            return EventResult.HANDLED;
        } else if (event.isKey(KeyCode.ENTER) || event.isChar(' ')) {
            toggleStatus();
            return EventResult.HANDLED;
        } else {
            return EventResult.UNHANDLED;
        }
    }

    @Override
    public Size preferredSize(int availableWidth, int availableHeight, RenderContext context) {
        return Size.UNKNOWN;
    }
}
