package com.bmstu_bureau_1440.library.ui.statistics;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.shared.io.MenuSelector;

import jakarta.annotation.PostConstruct;

@Component
public class StatisticsOperationsSelector extends MenuSelector {

    @Autowired
    private ObjectProvider<ShowTrendingBooksOperationExecutor> showTrendingBooksOperationExecutorProvider;

    @PostConstruct
    private void init() {
        executors.put(StatisticsOperations.SHOW_TRENDING_BOOKS, showTrendingBooksOperationExecutorProvider.getObject());
    }

    @Override
    protected String getLabel() {
        return "Выберите отчет:";
    }

}
