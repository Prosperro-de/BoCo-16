package com.bobocode.context;

import com.bobocode.context.aplicationcontext.ApplicationContextImpl;


import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        var context = new ApplicationContextImpl("com.bobocode.context");

        var bean = context.getBean(PrintService.class);
        bean.printHello();
    }
}
