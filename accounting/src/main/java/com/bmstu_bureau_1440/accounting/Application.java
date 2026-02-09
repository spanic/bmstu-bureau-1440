package com.bmstu_bureau_1440.accounting;

import com.bmstu_bureau_1440.accounting.services.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Application {

    private AnalyticsService service;

    public void run() {
        this.service.execute();
    }

}
