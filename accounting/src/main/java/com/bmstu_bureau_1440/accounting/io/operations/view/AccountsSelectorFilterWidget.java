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
public class AccountsSelectorFilterWidget extends AbstractFilterWidget {

    private final OperationsTuiController controller;

    @Override
    protected List<ItemData> getItemsData() {
        return controller.getAccounts()
                .stream()
                .map(account -> new ItemData(
                        account.getName(),
                        account.getId(),
                        controller.isAccountSelected(account.getId()) ? Status.SELECTED : Status.NOT_SELECTED))
                .toList();
    }

    @Override
    protected void onItemSelected(ItemData itemData) {
        controller.toggleAccountSelection(itemData.getKey());
    }
}
