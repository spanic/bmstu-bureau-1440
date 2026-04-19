package com.bmstu_bureau_1440.accounting.io.operations.view;

import com.bmstu_bureau_1440.accounting.io.operations.controller.OperationsTuiController;

import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.terminal.Frame;
import dev.tamboui.text.Line;
import dev.tamboui.text.Span;
import dev.tamboui.text.Text;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.Size;
import dev.tamboui.toolkit.element.StyledElement;
import dev.tamboui.widgets.paragraph.Paragraph;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TotalSummaryWidget extends StyledElement<TotalSummaryWidget> {

    private final OperationsTuiController controller;

    @Override
    protected void renderContent(Frame frame, Rect area, RenderContext renderContext) {

        final Paragraph paragraph = Paragraph.from(
                Text.from(
                        Line.from(
                                new Span("Total balance: ", Style.EMPTY.fg(Color.YELLOW).bold()),
                                Span.raw(controller.getAnalytics().totalBalance().toString())),
                        Line.from(
                                new Span("Total income: ", Style.EMPTY.fg(Color.LIGHT_CYAN).bold()),
                                Span.raw(controller.getAnalytics().totalIncome().toString())),
                        Line.from(
                                new Span("Total expenses: ", Style.EMPTY.fg(Color.BRIGHT_WHITE).bold()),
                                Span.raw(controller.getAnalytics().totalExpenses().toString()))));

        frame.renderWidget(paragraph, area);
    }

    @Override
    public Size preferredSize(int availableWidth, int availableHeight, RenderContext context) {
        return Size.UNKNOWN;
    }

}