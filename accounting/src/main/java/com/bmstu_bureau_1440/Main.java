package com.bmstu_bureau_1440;

import com.bmstu_bureau_1440.accounting.AppConfig;
import com.bmstu_bureau_1440.accounting.Application;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        var application = context.getBean(Application.class);

        application.run();
    }

}