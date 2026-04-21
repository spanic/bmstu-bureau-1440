package com.bmstu_bureau_1440.accounting.io.accounts.view;

import static dev.tamboui.toolkit.Toolkit.formField;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.accounting.io.accounts.controller.AccountsTuiController;
import com.bmstu_bureau_1440.accounting.io.common.utils.TuiUtils;
import com.bmstu_bureau_1440.accounting.io.shared.InputFields;
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
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.Size;
import dev.tamboui.toolkit.element.StyledElement;
import dev.tamboui.toolkit.elements.FormFieldElement;
import dev.tamboui.widgets.form.Validators;
import dev.tamboui.widgets.paragraph.Paragraph;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountDetailsWidget extends StyledElement<AccountDetailsWidget> {

    private final AccountsTuiController controller;

    private static final Layout layout = Layout.vertical()
            .constraints(
                    Constraint.length(1), // ID
                    Constraint.length(5), // Name (editable)
                    Constraint.length(5) // Balance (editable)
            );

    private static final Layout innerRowLayout = Layout.horizontal().constraints(
            Constraint.max(12), // Field name
            Constraint.fill() // Field value | input
    ).flex(Flex.CENTER);

    @Override
    protected void renderContent(Frame frame, Rect area, RenderContext renderContext) {
        final BankAccount selectedBankAccount = controller.getSelectedBankAccount();

        List<Rect> areas = layout.split(area);

        List<Rect> idFieldInnerAreas = innerRowLayout.split(areas.get(0));

        frame.renderWidget(
                Paragraph.from(Span.raw("ID: ").bold()),
                idFieldInnerAreas.getFirst());

        if (selectedBankAccount != null) {
            frame.renderWidget(
                    Paragraph.from(
                            CharWidth.truncateWithEllipsis(
                                    selectedBankAccount.getId(),
                                    idFieldInnerAreas.getLast().width(),
                                    CharWidth.TruncatePosition.END)),
                    idFieldInnerAreas.getLast());
        }

        List<Rect> nameFieldInnerAreas = innerRowLayout.split(areas.get(1));

        frame.renderWidget(
                Paragraph.from(Span.raw("Name: ").bold()),
                nameFieldInnerAreas.getFirst());

        FormFieldElement nameField = formField("",
                controller.getForm().textField(InputFields.ACCOUNT_NAME.getFieldName()))
                .formState(controller.getForm(), InputFields.ACCOUNT_NAME.getFieldName())
                .id(InputFields.ACCOUNT_NAME.getFieldId())
                .focusable()
                .labelWidth(Size.ZERO.width())
                .spacing(Size.ZERO.width())
                .rounded()
                .borderColor(Color.DARK_GRAY)
                .focusedBorderColor(Color.MAGENTA)
                .placeholder("Enter account name")
                .validate(
                        Validators.required("Name cannot be empty"),
                        Validators.minLength(3, "Too short"))
                .errorBorderColor(Color.RED)
                .showInlineErrors(true)
                .onSubmit(controller::createOrUpdateAccount);

        nameField.validateField();

        renderContext.renderChild(nameField, frame, nameFieldInnerAreas.get(1));

        List<Rect> balanceInnerAreas = innerRowLayout.split(areas.get(2));

        frame.renderWidget(
                Paragraph.from(Span.raw("Balance: ").bold()),
                balanceInnerAreas.getFirst());

        if (selectedBankAccount != null) {

            frame.renderWidget(
                    Paragraph.from(
                            Line.from(
                                    Span.raw("₽").yellow().bold(),
                                    Span.raw(selectedBankAccount.getBalance()
                                            .toString()).yellow().bold())),
                    balanceInnerAreas.getLast());

        } else {

            FormFieldElement balanceField = formField("",
                    controller.getForm().textField(InputFields.ACCOUNT_BALANCE.getFieldName()))
                    .formState(controller.getForm(), InputFields.ACCOUNT_BALANCE.getFieldName())
                    .id(InputFields.ACCOUNT_BALANCE.getFieldId())
                    .focusable()
                    .labelWidth(Size.ZERO.width())
                    .spacing(Size.ZERO.width())
                    .rounded()
                    .borderColor(Color.DARK_GRAY)
                    .focusedBorderColor(Color.MAGENTA)
                    .placeholder("Enter initial balance")
                    .validate(
                            Validators.minLength(1, "Cannot be empty"),
                            Validators.pattern("^\\d+\\.?\\d*$", "Only digits are allowed"))
                    .errorBorderColor(Color.RED)
                    .showInlineErrors(true)
                    .onSubmit(controller::createOrUpdateAccount);

            balanceField.validateField();

            renderContext.renderChild(balanceField, frame, balanceInnerAreas.get(1));
        }
    }

    @Override
    public Size preferredSize(int availableWidth, int availableHeight, RenderContext renderContext) {
        return TuiUtils.computePreferredSize(layout.constraints().toArray(new Constraint[0]), availableHeight);
    }
}
