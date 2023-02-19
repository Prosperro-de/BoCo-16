package com.bobocode.context;

import com.bobocode.context.annotation.Bean;
import com.bobocode.context.annotation.Inject;

@Bean
public class InjectionDemoService {

    @Inject
    PrintService printService;

    void sayHelloFromDependency() {
        printService.printHello();
    }
}
