package com.bmstu_bureau_1440.accounting.io.operations.view;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.accounting.io.common.ItemData;
import com.bmstu_bureau_1440.accounting.io.common.ItemData.Status;
import com.bmstu_bureau_1440.accounting.io.common.widgets.AbstractFilterWidget;
import com.bmstu_bureau_1440.accounting.io.operations.controller.OperationsTuiController;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoriesSelectorFilterWidget extends AbstractFilterWidget {

    private final OperationsTuiController controller;

    @Override
    protected List<ItemData> getItemsData() {
        return controller.getCategories()
                .stream()
                .map(category -> new ItemData(category.getName(), category.getId(),
                        controller.isCategorySelected(category.getId()) ? Status.SELECTED : Status.NOT_SELECTED))
                .toList();
    }

    @Override
    protected void onItemSelected(ItemData itemData) {
        controller.toggleCategorySelection(itemData.getKey());
    }
}
