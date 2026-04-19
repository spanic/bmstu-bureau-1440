package com.bmstu_bureau_1440.accounting.io.operations.view;

import static dev.tamboui.toolkit.Toolkit.formField;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.accounting.io.common.utils.TuiUtils;
import com.bmstu_bureau_1440.accounting.io.operations.controller.OperationsTuiController;
import com.bmstu_bureau_1440.accounting.io.shared.InputFields;
import com.bmstu_bureau_1440.accounting.models.Operation;

import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Flex;
import dev.tamboui.layout.Layout;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.terminal.Frame;
import dev.tamboui.text.CharWidth;
import dev.tamboui.text.Span;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.Size;
import dev.tamboui.toolkit.element.StyledElement;
import dev.tamboui.toolkit.elements.FormFieldElement;
import dev.tamboui.tui.bindings.KeyTrigger;
import dev.tamboui.tui.event.KeyCode;
import dev.tamboui.widgets.form.Validators;
import dev.tamboui.widgets.paragraph.Paragraph;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OperationsDetailsWidget extends StyledElement<OperationsDetailsWidget> {

    private final OperationsTuiController controller;

    private static final Layout layout = Layout.vertical()
            .constraints(
                    Constraint.length(1), // ID
                    Constraint.length(1), // Timestamp
                    Constraint.length(1), // Account ID
                    Constraint.length(1), // Category ID
                    Constraint.length(5), // Amount
                    Constraint.length(5) // Description
            );

    private static final Layout innerRowLayout = Layout.horizontal().constraints(
            Constraint.max(20), // Field name
            Constraint.fill() // Field value | input
    ).flex(Flex.CENTER);

    private static final Layout rowWithHintLayout = Layout.horizontal().constraints(
            Constraint.max(20), // Field name
            Constraint.max(42), // Select field
            Constraint.max(22), // Dimmed resolved name
            Constraint.fill() // Right spacer
    ).flex(Flex.CENTER);

    @Override
    protected void renderContent(Frame frame, Rect area, RenderContext renderContext) {
        final Operation selectedOperation = controller.getSelectedOperation();

        List<Rect> areas = layout.split(area);

        renderReadonlyField(frame, areas.get(0), "ID: ",
                selectedOperation == null ? StringUtils.EMPTY : selectedOperation.getId());
        renderReadonlyField(frame, areas.get(1), "Timestamp: ",
                selectedOperation == null ? StringUtils.EMPTY : selectedOperation.getDate().toString());

        renderAccountSelectField(frame, areas.get(2), renderContext);
        renderCategorySelectField(frame, areas.get(3), renderContext);
        renderAmountField(frame, areas.get(4), renderContext, selectedOperation);
        renderDescriptionField(frame, areas.get(5), renderContext);
    }

    private void renderReadonlyField(Frame frame, Rect rowArea, String title, String value) {
        List<Rect> innerAreas = innerRowLayout.split(rowArea);
        frame.renderWidget(Paragraph.from(Span.raw(title).bold()), innerAreas.getFirst());
        frame.renderWidget(
                Paragraph.from(
                        CharWidth.truncateWithEllipsis(
                                value,
                                innerAreas.getLast().width(),
                                CharWidth.TruncatePosition.END)),
                innerAreas.getLast());
    }

    private void renderAccountSelectField(Frame frame, Rect rowArea, RenderContext renderContext) {
        List<Rect> innerAreas = rowWithHintLayout.split(rowArea);

        frame.renderWidget(Paragraph.from(Span.raw("Account ID: ").bold()), innerAreas.get(0));

        FormFieldElement accountField = formField("",
                controller.getForm().selectField(InputFields.OPERATION_ACCOUNT.getFieldName()))
                .formState(controller.getForm(), InputFields.OPERATION_ACCOUNT.getFieldName())
                .id(InputFields.OPERATION_ACCOUNT.getFieldId())
                .labelWidth(Size.ZERO.width())
                .spacing(Size.ZERO.width())
                .focusable()
                .on(KeyTrigger.key(KeyCode.ENTER), e -> controller.createOrUpdateOperation());

        renderContext.renderChild(accountField, frame, innerAreas.get(1));

        String accountName = resolveSelectedAccountName();

        frame.renderWidget(
                Paragraph.from(
                        Span.raw(CharWidth.truncateWithEllipsis(accountName, innerAreas.get(2).width(),
                                CharWidth.TruncatePosition.END)).dim()),
                innerAreas.get(2));
    }

    private void renderCategorySelectField(Frame frame, Rect rowArea, RenderContext renderContext) {
        List<Rect> innerAreas = rowWithHintLayout.split(rowArea);

        frame.renderWidget(Paragraph.from(Span.raw("Category ID: ").bold()), innerAreas.get(0));

        FormFieldElement categoryField = formField("",
                controller.getForm().selectField(InputFields.OPERATION_CATEGORY.getFieldName()))
                .formState(controller.getForm(), InputFields.OPERATION_CATEGORY.getFieldName())
                .id(InputFields.OPERATION_CATEGORY.getFieldId())
                .labelWidth(Size.ZERO.width())
                .spacing(Size.ZERO.width())
                .focusable()
                .on(KeyTrigger.key(KeyCode.ENTER), e -> controller.createOrUpdateOperation());

        renderContext.renderChild(categoryField, frame, innerAreas.get(1));

        String categoryName = resolveSelectedCategoryName();

        frame.renderWidget(
                Paragraph.from(
                        Span.raw(CharWidth.truncateWithEllipsis(
                                categoryName,
                                innerAreas.get(2).width(),
                                CharWidth.TruncatePosition.END)).dim()),
                innerAreas.get(2));
    }

    private void renderAmountField(Frame frame, Rect rowArea, RenderContext renderContext,
            Operation selectedOperation) {
        List<Rect> innerAreas = innerRowLayout.split(rowArea);

        frame.renderWidget(Paragraph.from(Span.raw("Amount: ").bold()), innerAreas.getFirst());

        if (selectedOperation != null) {
            frame.renderWidget(
                    Paragraph.from(
                            Span.raw(CharWidth.truncateWithEllipsis(
                                    selectedOperation.getAmount().toPlainString(),
                                    innerAreas.getLast().width(),
                                    CharWidth.TruncatePosition.END)).yellow().bold()),
                    innerAreas.getLast());
            return;
        }

        FormFieldElement amountField = formField("",
                controller.getForm().textField(InputFields.OPERATION_AMOUNT.getFieldName()))
                .formState(controller.getForm(), InputFields.OPERATION_AMOUNT.getFieldName())
                .id(InputFields.OPERATION_AMOUNT.getFieldId())
                .focusable()
                .labelWidth(Size.ZERO.width())
                .spacing(Size.ZERO.width())
                .rounded()
                .borderColor(Color.DARK_GRAY)
                .focusedBorderColor(Color.MAGENTA)
                .placeholder("Enter amount")
                .validate(
                        Validators.minLength(1, "Cannot be empty"),
                        Validators.pattern("^\\d+\\.?\\d*$", "Only digits are allowed"))
                .errorBorderColor(Color.RED)
                .showInlineErrors(true)
                .onSubmit(controller::createOrUpdateOperation);

        amountField.validateField();

        renderContext.renderChild(amountField, frame, innerAreas.get(1));
    }

    private void renderDescriptionField(Frame frame, Rect rowArea, RenderContext renderContext) {
        List<Rect> innerAreas = innerRowLayout.split(rowArea);

        frame.renderWidget(Paragraph.from(Span.raw("Description: ").bold()), innerAreas.getFirst());

        FormFieldElement descriptionField = formField("",
                controller.getForm().textField(InputFields.OPERATION_DESCRIPTION.getFieldName()))
                .formState(controller.getForm(), InputFields.OPERATION_DESCRIPTION.getFieldName())
                .id(InputFields.OPERATION_DESCRIPTION.getFieldId())
                .focusable()
                .labelWidth(Size.ZERO.width())
                .spacing(Size.ZERO.width())
                .rounded()
                .borderColor(Color.DARK_GRAY)
                .focusedBorderColor(Color.MAGENTA)
                .placeholder("Enter operation description")
                .onSubmit(controller::createOrUpdateOperation);

        renderContext.renderChild(descriptionField, frame, innerAreas.get(1));
    }

    private String resolveSelectedAccountName() {
        String accountId = controller.getForm().selectValue(InputFields.OPERATION_ACCOUNT.getFieldName());
        if (StringUtils.isBlank(accountId)) {
            return StringUtils.EMPTY;
        }

        try {
            return controller.getAccountById(accountId).getName();
        } catch (RuntimeException ignored) {
            return StringUtils.EMPTY;
        }
    }

    private String resolveSelectedCategoryName() {
        String categoryId = controller.getForm().selectValue(InputFields.OPERATION_CATEGORY.getFieldName());
        if (StringUtils.isBlank(categoryId)) {
            return StringUtils.EMPTY;
        }

        try {
            return controller.getCategoryById(categoryId).getName();
        } catch (RuntimeException ignored) {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public Size preferredSize(int availableWidth, int availableHeight, RenderContext renderContext) {
        return TuiUtils.computePreferredSize(layout.constraints().toArray(new Constraint[0]), availableHeight);
    }
}
