package com.bmstu_bureau_1440.accounting.io;

import com.bmstu_bureau_1440.accounting.io.controller.AccountingTuiController;
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
import dev.tamboui.widgets.form.Validators;
import dev.tamboui.widgets.paragraph.Paragraph;

import java.util.List;

import static dev.tamboui.toolkit.Toolkit.formField;

public class AccountDetailsWidget extends StyledElement<AccountDetailsWidget> {

    private final AccountingTuiController controller;

    private static final Layout layout = Layout.vertical()
            .constraints(
                    Constraint.length(1), // ID
                    Constraint.length(5), // Name (editable)
                    Constraint.length(1)  // Balance
            );

    private static final Layout innerRowLayout = Layout.horizontal().constraints(
            Constraint.max(12), // Field name
            Constraint.fill() // Field value | input
    ).flex(Flex.CENTER);

    public AccountDetailsWidget(AccountingTuiController controller) {
        this.controller = controller;
    }

    @Override
    protected void renderContent(Frame frame, Rect area, RenderContext renderContext) {
        final BankAccount selectedBankAccount = controller.getSelectedBankAccount();

        List<Rect> areas = layout.split(area);

        List<Rect> idInnerAreas = innerRowLayout.split(areas.get(0));

        frame.renderWidget(
                Paragraph.from(Text.from(Line.from(Span.raw("ID: ").bold()))),
                idInnerAreas.getFirst()
        );
        frame.renderWidget(
                Paragraph.from(Text.from(Line.from(Span.raw(
                                CharWidth.truncateWithEllipsis(
                                        selectedBankAccount.getId(),
                                        idInnerAreas.getLast().width(),
                                        CharWidth.TruncatePosition.END
                                )
                        ).cyan()))
                ),
                idInnerAreas.getLast()
        );

        List<Rect> nameFieldInnerAreas = innerRowLayout.split(areas.get(1));

        Element nameField = formField("", controller.getForm().textField("name"))
                .formState(controller.getForm(), "name")
                .id("name-input")
                .focusable()
                .labelWidth(Size.ZERO.width())
                .spacing(Size.ZERO.width())
                .rounded()
                .borderColor(Color.DARK_GRAY)
                .focusedBorderColor(Color.MAGENTA)
                .placeholder("Enter account name")
                .validate(
                        Validators.required("Name cannot be empty"),
                        Validators.minLength(3, "Too short")
                )
                .errorBorderColor(Color.RED)
                .showInlineErrors(true)
                .onSubmit(() -> {
                    boolean isNameValid = controller.getForm().validationResult("name").isValid();
                    if (!isNameValid) {
                        return;
                    }
                    String value = controller.getForm().textValue("name");
                    selectedBankAccount.setName(value);
                });

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
                                Span.raw(selectedBankAccount.getBalance().toString()).yellow().bold()
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
