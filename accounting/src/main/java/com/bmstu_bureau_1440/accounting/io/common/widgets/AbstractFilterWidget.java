package com.bmstu_bureau_1440.accounting.io.common.widgets;

import java.util.ArrayList;
import java.util.List;

import com.bmstu_bureau_1440.accounting.io.common.ItemData;
import com.bmstu_bureau_1440.accounting.io.common.ItemData.Status;

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

public abstract class AbstractFilterWidget extends StyledElement<AbstractFilterWidget> {

    private final ListState listState = new ListState();
    private List<ItemData> items = new ArrayList<>();

    protected abstract List<ItemData> getItemsData();

    protected abstract void onItemSelected(ItemData itemData);

    @Override
    protected void renderContent(Frame frame, Rect area, RenderContext renderContext) {
        setItemsData();
        renderList(frame, area);
    }

    private void setItemsData() {
        this.items = getItemsData();
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

    private List<ListItem> buildListItems() {
        List<ListItem> items = new ArrayList<>();

        for (int i = 0; i < this.items.size(); i++) {

            ItemData itemData = this.items.get(i);

            Color bgColor = (i % 2 == 0)
                    ? Color.indexed(235) // Dark gray (similar to SLATE.c950)
                    : Color.indexed(236); // Slightly lighter (similar to SLATE.c900)

            Text line;
            if (itemData.getStatus() == Status.NOT_SELECTED) {
                line = MarkupParser.parse("[light-gray] :black_square_button: " + itemData.getTitle());
            } else {
                line = MarkupParser.parse("[green] :white_check_mark: " + itemData.getTitle());
            }

            ListItem item = ListItem.from(line).style(Style.EMPTY.bg(bgColor));

            items.add(item);
        }

        return items;
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
            onItemSelected(items.get(listState.selected()));
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
