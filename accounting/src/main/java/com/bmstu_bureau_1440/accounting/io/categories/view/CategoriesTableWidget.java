package com.bmstu_bureau_1440.accounting.io.categories.view;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.accounting.io.categories.controller.CategoriesTuiController;
import com.bmstu_bureau_1440.accounting.io.common.Column;
import com.bmstu_bureau_1440.accounting.io.common.widgets.AbstractTableWidget;
import com.bmstu_bureau_1440.accounting.io.common.widgets.ConfirmationDialogWidget;
import com.bmstu_bureau_1440.accounting.models.Category;

import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Rect;
import dev.tamboui.terminal.Frame;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widgets.table.TableState;

@Component
public class CategoriesTableWidget extends AbstractTableWidget<Category, CategoriesTuiController> {

    public CategoriesTableWidget(CategoriesTuiController controller) {
        super(controller);
    }

    @Override
    protected Function<CategoriesTuiController, TableState> getStateProvider() {
        return CategoriesTuiController::getCategoriesTableState;
    }

    @Override
    protected List<Column<Category>> getColumns() {
        return List.of(
                new Column<>("ID", Constraint.percentage(40), Category::getId),
                new Column<>("Type", Constraint.percentage(20), category -> category.getType().toString()),
                new Column<>("Name", Constraint.fill(), Category::getName));
    }

    @Override
    protected Function<CategoriesTuiController, List<Category>> getDataProvider() {
        return CategoriesTuiController::getCategories;
    }

    @Override
    protected void renderContent(Frame frame, Rect rect, RenderContext renderContext) {
        super.renderContent(frame, rect, renderContext);

        if (controller.getRemoveCategoryDialogVisible()) {
            renderContext.renderChild(
                    new ConfirmationDialogWidget(controller::removeCategory, () -> {
                        controller.setRemoveCategoryDialogVisible(false);
                    }),
                    frame, rect);
        }
    }

    @Override
    public EventResult handleKeyEvent(KeyEvent event, boolean focused) {
        if (!focused) {
            return EventResult.UNHANDLED;
        }

        if (event.isUp()) {
            controller.selectPreviousCategory();
            return EventResult.HANDLED;
        } else if (event.isDown()) {
            controller.selectNextCategory();
            return EventResult.HANDLED;
        } else if (event.isChar('d')) {
            controller.showDeleteConfirmationPopup();
            return EventResult.HANDLED;
        } else if (event.isChar('c')) {
            controller.clearCategorySelection();
            return EventResult.HANDLED;
        } else {
            return EventResult.UNHANDLED;
        }
    }
}
