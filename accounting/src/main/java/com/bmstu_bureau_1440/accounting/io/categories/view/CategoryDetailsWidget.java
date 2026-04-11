package com.bmstu_bureau_1440.accounting.io.categories.view;

import com.bmstu_bureau_1440.accounting.io.categories.controller.CategoriesTuiController;
import com.bmstu_bureau_1440.accounting.io.common.utils.TuiUtils;
import com.bmstu_bureau_1440.accounting.io.shared.InputFields;
import com.bmstu_bureau_1440.accounting.models.Category;
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

import java.util.List;

import static dev.tamboui.toolkit.Toolkit.formField;

@RequiredArgsConstructor
public class CategoryDetailsWidget extends StyledElement<CategoryDetailsWidget> {

    private final CategoriesTuiController controller;

    private static final Layout layout = Layout.vertical()
            .constraints(
                    Constraint.length(1), // ID
                    Constraint.length(1), // Type
                    Constraint.length(5)  // Name (editable)
            );

    private static final Layout innerRowLayout = Layout.horizontal().constraints(
            Constraint.max(12), // Field name
            Constraint.fill() // Field value | input
    ).flex(Flex.CENTER);

    @Override
    protected void renderContent(Frame frame, Rect area, RenderContext renderContext) {

        final Category selectedCategory = controller.getSelectedCategory();

        List<Rect> areas = layout.split(area);

        List<Rect> idFieldInnerAreas = innerRowLayout.split(areas.get(0));

        frame.renderWidget(
                Paragraph.from(Span.raw("ID: ").bold()),
                idFieldInnerAreas.getFirst()
        );

        if (selectedCategory != null) {
            frame.renderWidget(
                    Paragraph.from(
                            CharWidth.truncateWithEllipsis(
                                    selectedCategory.getId(),
                                    idFieldInnerAreas.getLast().width(),
                                    CharWidth.TruncatePosition.END
                            )
                    ),
                    idFieldInnerAreas.getLast()
            );
        }

        List<Rect> typeFieldInnerAreas = innerRowLayout.split(areas.get(1));

        frame.renderWidget(
                Paragraph.from(Span.raw("Type: ").bold()),
                typeFieldInnerAreas.getFirst()
        );

        FormFieldElement selectTypeField = formField("", controller.getForm().selectField(InputFields.CATEGORY_TYPE.getFieldName()))
                .formState(controller.getForm(), InputFields.CATEGORY_TYPE.getFieldName())
                .id(InputFields.CATEGORY_TYPE.getFieldId())
                .labelWidth(Size.ZERO.width())
                .spacing(Size.ZERO.width())
                .focusable()
                .on(KeyTrigger.key(KeyCode.ENTER), e -> controller.createOrUpdateCategory());

        renderContext.renderChild(selectTypeField, frame, typeFieldInnerAreas.get(1));

        List<Rect> nameFieldInnerAreas = innerRowLayout.split(areas.get(2));

        frame.renderWidget(
                Paragraph.from(Span.raw("Name: ").bold()),
                nameFieldInnerAreas.getFirst()
        );

        FormFieldElement nameField = formField("", controller.getForm().textField(InputFields.CATEGORY_NAME.getFieldName()))
                .formState(controller.getForm(), InputFields.CATEGORY_NAME.getFieldName())
                .id(InputFields.CATEGORY_NAME.getFieldId())
                .focusable()
                .labelWidth(Size.ZERO.width())
                .spacing(Size.ZERO.width())
                .rounded()
                .borderColor(Color.DARK_GRAY)
                .focusedBorderColor(Color.MAGENTA)
                .placeholder("Enter category name")
                .validate(
                        Validators.required("Name cannot be empty"),
                        Validators.minLength(3, "Too short")
                )
                .errorBorderColor(Color.RED)
                .showInlineErrors(true)
                .onSubmit(controller::createOrUpdateCategory);

        nameField.validateField();

        renderContext.renderChild(nameField, frame, nameFieldInnerAreas.get(1));

    }

    @Override
    public Size preferredSize(int availableWidth, int availableHeight, RenderContext renderContext) {
        return TuiUtils.computePreferredSize(layout.constraints().toArray(new Constraint[0]), availableHeight);
    }
}
