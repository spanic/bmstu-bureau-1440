package com.bmstu_bureau_1440.accounting.io.categories.controller;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.io.common.utils.TuiUtils;
import com.bmstu_bureau_1440.accounting.io.shared.InputFields;
import com.bmstu_bureau_1440.accounting.models.Category;
import com.bmstu_bureau_1440.accounting.models.OperationType;
import com.bmstu_bureau_1440.accounting.services.CategoryOperationsService;
import dev.tamboui.widgets.form.FormState;
import dev.tamboui.widgets.table.TableState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class CategoriesTuiController {

    private final Storage storage;

    private final CategoryOperationsService categoryOperationsService;

    @Getter
    private final TableState categoriesTableState = new TableState();

    @Getter
    @Setter
    private Boolean removeCategoryDialogVisible = false;

    @Getter
    private Category selectedCategory;

    @Getter
    private final FormState form = FormState.builder()
            .textField(InputFields.CATEGORY_NAME.getFieldName(), "")
            .selectField(InputFields.CATEGORY_TYPE.getFieldName(), Stream.of(OperationType.values()).map(Enum::toString).toList())
            .build();

    // Queries
    public List<Category> getCategories() {
        return storage.getCategories();
    }

    // Commands
    public void selectPreviousCategory() {
        categoriesTableState.selectPrevious();
        selectedCategory = TuiUtils.getSelectedObject(categoriesTableState, getCategories());
        updateEditAccountForm();
        setRemoveCategoryDialogVisible(false);
    }

    public void selectNextCategory() {
        categoriesTableState.selectNext(getCategories().size());
        selectedCategory = TuiUtils.getSelectedObject(categoriesTableState, getCategories());
        updateEditAccountForm();
        setRemoveCategoryDialogVisible(false);
    }

    public void clearCategorySelection() {
        categoriesTableState.clearSelection();
        selectedCategory = null;
        updateEditAccountForm();
    }

    public void createOrUpdateCategory() {
        boolean isNameValid = getForm().validationResult(InputFields.CATEGORY_NAME.getFieldName()).isValid();

        if (!isNameValid) {
            return;
        }

        String name = getForm().textValue(InputFields.CATEGORY_NAME.getFieldName());
        OperationType type = OperationType.valueOf(getForm().selectValue(InputFields.CATEGORY_TYPE.getFieldName()));

        if (ObjectUtils.isNotEmpty(selectedCategory)) {
            selectedCategory.setName(name);
            selectedCategory.setType(type);
        } else {
            categoryOperationsService.addNewCategory(name, type);
            categoriesTableState.selectLast(getCategories().size());
            selectedCategory = TuiUtils.getSelectedObject(categoriesTableState, getCategories());
        }
    }

    public void removeCategory() {
        categoryOperationsService.deleteCategory(selectedCategory);

        setRemoveCategoryDialogVisible(false);

        if (categoriesTableState.selected() == getCategories().size()) {
            if (getCategories().isEmpty()) {
                clearCategorySelection();
            } else {
                selectPreviousCategory();
            }
        } else {
            selectedCategory = TuiUtils.getSelectedObject(categoriesTableState, getCategories());
            updateEditAccountForm();
        }
    }

    private void updateEditAccountForm() {
        form.setTextValue(InputFields.CATEGORY_NAME.getFieldName(), selectedCategory == null ?
                StringUtils.EMPTY : selectedCategory.getName());
        form.textField(InputFields.CATEGORY_NAME.getFieldName()).moveCursorToEnd();
    }

}
