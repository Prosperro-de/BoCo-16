package com.bobocode.context.aplicationcontext;

import java.util.Map;

public interface ApplicationContext {
    <T> T getBean(Class<T> beanType);
    <T> T getBean(String beanName, Class<T> beanType);
    <T> Map<String, T> getAllBeans(Class<T> beanType);
}
