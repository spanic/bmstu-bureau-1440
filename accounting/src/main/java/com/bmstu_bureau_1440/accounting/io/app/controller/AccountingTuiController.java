package com.bmstu_bureau_1440.accounting.io.app.controller;

import dev.tamboui.widgets.tabs.TabsState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AccountingTuiController {

    @Getter
    private final TabsState mainNavigationTabsState = new TabsState(0);

}
