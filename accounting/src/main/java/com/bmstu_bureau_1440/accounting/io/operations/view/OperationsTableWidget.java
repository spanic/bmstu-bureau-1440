package com.bmstu_bureau_1440.accounting.io.operations.view;

import java.util.List;
import java.util.function.Function;

import com.bmstu_bureau_1440.accounting.io.common.Column;
import com.bmstu_bureau_1440.accounting.io.common.widgets.AbstractTableWidget;
import com.bmstu_bureau_1440.accounting.io.common.widgets.ConfirmationDialogWidget;
import com.bmstu_bureau_1440.accounting.io.operations.controller.OperationsTuiController;
import com.bmstu_bureau_1440.accounting.models.Operation;

import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Rect;
import dev.tamboui.terminal.Frame;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widgets.table.TableState;

public class OperationsTableWidget extends AbstractTableWidget<Operation, OperationsTuiController> {

    public OperationsTableWidget(OperationsTuiController controller) {
        super(controller);
    }

    @Override
    protected Function<OperationsTuiController, TableState> getStateProvider() {
        return OperationsTuiController::getOperationsTableState;
    }

    @Override
    protected List<Column<Operation>> getColumns() {
        return List.of(
                new Column<>("Timestamp", Constraint.percentage(30), operation -> operation.getDate().toString()),
                new Column<>("Account", Constraint.percentage(15),
                        operation -> controller.getAccountById(operation.getBankAccountId()).getName()),
                new Column<>("Category", Constraint.percentage(15),
                        operation -> controller.getCategoryById(operation.getCategoryId()).getName()),
                new Column<>("Amount", Constraint.percentage(10), operation -> operation.getAmount().toPlainString()),
                new Column<>("Description", Constraint.fill(), Operation::getDescription));
    }

    @Override
    protected Function<OperationsTuiController, List<Operation>> getDataProvider() {
        return OperationsTuiController::getOperations;
    }

    @Override
    protected void renderContent(Frame frame, Rect rect, RenderContext renderContext) {
        super.renderContent(frame, rect, renderContext);

        if (controller.getRemoveOperationDialogVisible()) {
            renderContext.renderChild(
                    new ConfirmationDialogWidget(controller::removeOperation, () -> {
                        controller.setRemoveOperationDialogVisible(false);
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
            controller.selectPreviousOperation();
            return EventResult.HANDLED;
        } else if (event.isDown()) {
            controller.selectNextOperation();
            return EventResult.HANDLED;
        } else if (event.isChar('d')) {
            controller.showDeleteConfirmationPopup();
            return EventResult.HANDLED;
        } else if (event.isChar('c')) {
            controller.clearOperationSelection();
            return EventResult.HANDLED;
        } else {
            return EventResult.UNHANDLED;
        }
    }
}
