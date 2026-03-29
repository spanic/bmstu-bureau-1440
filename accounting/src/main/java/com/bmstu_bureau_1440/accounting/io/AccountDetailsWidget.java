package com.bmstu_bureau_1440.accounting.io;

import com.bmstu_bureau_1440.accounting.models.BankAccount;
import dev.tamboui.layout.Rect;
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
public class AccountDetailsWidget extends StyledElement<AccountDetailsWidget> {

    private final BankAccount account;

    @Override
    protected void renderContent(Frame frame, Rect area, RenderContext renderContext) {

        Text text = Text.from(
                Line.from(
                        Span.raw("ID: ").bold(),
                        Span.raw(account.getId()).cyan()
                ),
                Line.from(
                        Span.raw("Name: ").bold(),
                        Span.raw(account.getName()).magenta().bold()
                ),
                Line.from(
                        Span.raw("Balance: ").bold(),
                        Span.raw("₽").yellow().bold(),
                        Span.raw(account.getBalance().toString()).yellow().bold()
                )
        );

        var details = Paragraph.builder().text(text).build();

        frame.renderWidget(details, area);
    }

    @Override
    public Size preferredSize(int i, int i1, RenderContext renderContext) {
        return Size.heightOnly(3);
    }
}
