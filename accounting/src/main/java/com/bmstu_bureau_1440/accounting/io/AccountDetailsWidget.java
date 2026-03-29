package com.bmstu_bureau_1440.accounting.io;

import com.bmstu_bureau_1440.accounting.io.utils.TuiUtils;
import com.bmstu_bureau_1440.accounting.models.BankAccount;
import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Flex;
import dev.tamboui.layout.Layout;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.terminal.Frame;
import dev.tamboui.text.CharWidth;
import dev.tamboui.text.Line;
import dev.tamboui.text.Span;
import dev.tamboui.text.Text;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.Size;
import dev.tamboui.toolkit.element.StyledElement;
import dev.tamboui.widgets.form.FormState;
import dev.tamboui.widgets.paragraph.Paragraph;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static dev.tamboui.toolkit.Toolkit.textInput;

public class AccountDetailsWidget extends StyledElement<AccountDetailsWidget> {

    private static BankAccount currentBankAccount;

    private static final Layout layout = Layout.vertical()
            .constraints(
                    Constraint.length(1), // ID
                    Constraint.length(3), // Name (editable)
                    Constraint.length(1)  // Balance
            );

    private static final Layout innerRowLayout = Layout.horizontal().constraints(
            Constraint.max(12), // Field name
            Constraint.fill() // Field value | input
    ).flex(Flex.CENTER);

    private static final FormState form = FormState.builder()
            .textField("name", "")
            .build();

    public void setAccount(BankAccount account) {
        if (ObjectUtils.notEqual(account, currentBankAccount)) {
            currentBankAccount = account;
            form.setTextValue("name", ObjectUtils.isEmpty(account) ? StringUtils.EMPTY : account.getName());
            form.textField("name").moveCursorToEnd();
        }
    }

    @Override
    protected void renderContent(Frame frame, Rect area, RenderContext renderContext) {
        List<Rect> areas = layout.split(area);

        List<Rect> idInnerAreas = innerRowLayout.split(areas.get(0));

        frame.renderWidget(
                Paragraph.from(Text.from(Line.from(Span.raw("ID: ").bold()))),
                idInnerAreas.getFirst()
        );
        frame.renderWidget(
                Paragraph.from(Text.from(Line.from(Span.raw(
                                CharWidth.truncateWithEllipsis(
                                        currentBankAccount.getId(),
                                        idInnerAreas.getLast().width(),
                                        CharWidth.TruncatePosition.END
                                )
                        ).cyan()))
                ),
                idInnerAreas.getLast()
        );

        List<Rect> nameFieldInnerAreas = innerRowLayout.split(areas.get(1));

        Element nameField = textInput(form.textField("name"))
                .id("name-input")
                .focusable()
                .rounded()
                .borderColor(Color.DARK_GRAY)
                .focusedBorderColor(Color.MAGENTA)
                .placeholder("Enter account name");

        frame.renderWidget(
                Paragraph.from(Text.from(Line.from(Span.raw("Name: ").bold()))),
                nameFieldInnerAreas.getFirst()
        );
        renderContext.renderChild(nameField, frame, nameFieldInnerAreas.get(1));

        List<Rect> balanceInnerAreas = innerRowLayout.split(areas.get(2));

        frame.renderWidget(
                Paragraph.from(Text.from(Line.from(Span.raw("Balance: ").bold()))),
                balanceInnerAreas.getFirst()
        );
        frame.renderWidget(
                Paragraph.from(Text.from(
                        Line.from(
                                Span.raw("Balance: ").bold(),
                                Span.raw("₽").yellow().bold(),
                                Span.raw(currentBankAccount.getBalance().toString()).yellow().bold()
                        )
                )),
                balanceInnerAreas.getLast()
        );
    }

    @Override
    public Size preferredSize(int availableWidth, int availableHeight, RenderContext renderContext) {
        return TuiUtils.computePreferredSize(layout.constraints().toArray(new Constraint[0]), availableHeight);
    }
}
