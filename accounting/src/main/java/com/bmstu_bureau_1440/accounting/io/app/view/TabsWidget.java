package com.bmstu_bureau_1440.accounting.io.app.view;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.accounting.io.app.controller.AccountingTuiController;

import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.terminal.Frame;
import dev.tamboui.text.Span;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.Size;
import dev.tamboui.toolkit.element.StyledElement;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widgets.block.Block;
import dev.tamboui.widgets.block.BorderType;
import dev.tamboui.widgets.block.Borders;
import dev.tamboui.widgets.tabs.Tabs;

@Component
public class TabsWidget extends StyledElement<TabsWidget> {

    private final AccountingTuiController controller;

    public TabsWidget(AccountingTuiController controller) {
        this.controller = controller;
    }

    @Override
    public String id() {
        return "main-tabs-navigation";
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    @Override
    protected void renderContent(Frame frame, Rect area, RenderContext renderContext) {
        Tabs tabs = Tabs.builder()
                .titles("Accounts", "Transactions", "Categories")
                .style(Style.EMPTY.fg(Color.WHITE))
                .highlightStyle(Style.EMPTY.fg(Color.YELLOW).bold())
                .divider(Span.raw(" │ ").fg(Color.DARK_GRAY))
                .padding(" ", " ")
                .block(Block.builder()
                        .borders(Borders.ALL)
                        .borderType(BorderType.PLAIN)
                        .borderStyle(Style.EMPTY.fg(renderContext.isFocused(this.id()) ? Color.MAGENTA : Color.GREEN))
                        .build())
                .build();

        frame.renderStatefulWidget(tabs, area, controller.getMainNavigationTabsState());
    }

    @Override
    public EventResult handleKeyEvent(KeyEvent event, boolean focused) {
        if (!focused) {
            return EventResult.UNHANDLED;
        }

        if (event.isRight()) {
            controller.getMainNavigationTabsState().selectNext(3);
            return EventResult.HANDLED;
        } else if (event.isLeft()) {
            controller.getMainNavigationTabsState().selectPrevious(3);
            return EventResult.HANDLED;
        } else {
            return EventResult.UNHANDLED;
        }
    }

    @Override
    public Size preferredSize(int i, int i1, RenderContext renderContext) {
        return Size.heightOnly(3);
    }
}
