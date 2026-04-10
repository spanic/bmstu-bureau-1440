package com.bmstu_bureau_1440.accounting.io.controller;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.io.utils.TuiUtils;
import com.bmstu_bureau_1440.accounting.models.Category;
import com.bmstu_bureau_1440.accounting.services.CategoryOperationsService;
import dev.tamboui.widgets.table.TableState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.util.List;

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

    public List<Category> getCategories() {
        return storage.getCategories();
    }

    // Commands
    public void selectPreviousCategory() {
        categoriesTableState.selectPrevious();
        selectedCategory = TuiUtils.getSelectedObject(categoriesTableState, getCategories());
        setRemoveCategoryDialogVisible(false);
    }

    public void selectNextCategory() {
        categoriesTableState.selectNext(storage.getCategories().size());
        selectedCategory = TuiUtils.getSelectedObject(categoriesTableState, getCategories());
        setRemoveCategoryDialogVisible(false);
    }

    public void clearCategorySelection() {
        categoriesTableState.clearSelection();
        selectedCategory = null;
    }

    public void removeCategory() {
        categoryOperationsService.deleteCategory(selectedCategory);

        setRemoveCategoryDialogVisible(false);

        if (categoriesTableState.selected() == storage.getCategories().size()) {
            if (storage.getCategories().isEmpty()) {
                clearCategorySelection();
                setRemoveCategoryDialogVisible(false);
            } else {
                selectPreviousCategory();
            }
        } else {
            selectedCategory = TuiUtils.getSelectedObject(categoriesTableState, getCategories());
        }

    }

}
