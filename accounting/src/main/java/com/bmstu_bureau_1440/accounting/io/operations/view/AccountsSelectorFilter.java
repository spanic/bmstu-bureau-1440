package com.bmstu_bureau_1440.accounting.io.operations.view;

import java.util.ArrayList;
import java.util.List;

import com.bmstu_bureau_1440.accounting.io.common.ItemData;
import com.bmstu_bureau_1440.accounting.io.common.ItemData.Status;
import com.bmstu_bureau_1440.accounting.io.operations.controller.OperationsTuiController;

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
import dev.tamboui.widgets.list.ListWidget;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountsSelectorFilter extends StyledElement<AccountsSelectorFilter> {

    private final OperationsTuiController controller;

    private final List<ItemData> items = new ArrayList<>();

    @Override
    protected void renderContent(Frame frame, Rect area, RenderContext context) {
        buildItemsFromAccounts();
        renderList(frame, area);
    }

    private void buildItemsFromAccounts() {
        items.clear();

        controller.getAccounts().stream()
                .map(account -> new ItemData(
                        account.getName(),
                        account.getId(),
                        controller.isAccountSelected(account.getId()) ? Status.SELECTED : Status.NOT_SELECTED))
                .forEach(items::add);
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

        frame.renderStatefulWidget(list, area, controller.getAccountsFilterListState());
    }

    private List<ListItem> buildListItems() {
        List<ListItem> items = new ArrayList<>();
        for (int i = 0; i < this.items.size(); i++) {
            ItemData todoItem = this.items.get(i);
            Color bgColor = (i % 2 == 0)
                    ? Color.indexed(235) // Dark gray (similar to SLATE.c950)
                    : Color.indexed(236); // Slightly lighter (similar to SLATE.c900)

            Text line;
            if (todoItem.getStatus() == Status.NOT_SELECTED) {
                line = MarkupParser.parse("[light-gray] :black_square_button: " + todoItem.getTitle());
            } else {
                line = MarkupParser.parse("[green] :white_check_mark: " + todoItem.getTitle());
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

        buildItemsFromAccounts();

        if (event.isUp()) {
            controller.getAccountsFilterListState().selectPrevious();
            return EventResult.HANDLED;
        } else if (event.isDown()) {
            controller.getAccountsFilterListState().selectNext(items.size());
            return EventResult.HANDLED;
        } else if (event.isKey(KeyCode.ENTER) || event.isChar(' ')) {
            toggleStatus();
            return EventResult.HANDLED;
        } else {
            return EventResult.UNHANDLED;
        }
    }

    private void toggleStatus() {
        Integer selectedIdx = controller.getAccountsFilterListState().selected();
        if (selectedIdx != null && selectedIdx >= 0 && selectedIdx < items.size()) {
            ItemData selectedListItemData = items.get(selectedIdx);
            controller.toggleAccountSelection(selectedListItemData.getKey());
        }
    }

    @Override
    public Size preferredSize(int availableWidth, int availableHeight, RenderContext context) {
        return Size.UNKNOWN;
    }
}
