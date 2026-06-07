package com.bmstu_bureau_1440.library;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("Hello, World!");
    }
}
