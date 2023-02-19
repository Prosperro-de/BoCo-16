package com.bobocode.context;

import com.bobocode.context.annotation.Bean;

@Bean
public class PrintService {

    void printHello() {
        System.out.println("Hello from PrintService");
    }
}
