package com.bobocode.context.aplicationcontext;

import com.bobocode.context.annotation.Bean;
import com.bobocode.context.annotation.Inject;
import com.bobocode.context.exception.NoSuchBeanException;
import com.bobocode.context.exception.NoUniqueBeanException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class ApplicationContextImpl implements ApplicationContext {
    private Map<String, Object> context = new HashMap<>();

    public ApplicationContextImpl(String packageName) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotatedTypes = reflections.getTypesAnnotatedWith(Bean.class);
        for (Class<?> annotatedType : annotatedTypes) {
            String beanName = resolveBeanName(annotatedType);
            Constructor<?> constructor = annotatedType.getConstructor();
            var bean = constructor.newInstance();

            injectDependencies(bean);

            context.put(beanName, bean);
        }

    }

    private void injectDependencies(Object bean) throws IllegalAccessException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                field.set(bean, getBean(field.getType()));
            }
        }
    }


    public <T> T getBean(Class<T> beanType) {
        Map<String, T> matchingBeans = getAllBeans(beanType);
        if (matchingBeans.size() > 2) {
            throw new NoUniqueBeanException();
        }
        return matchingBeans.values()
                .stream().findFirst()
                .orElseThrow(NoSuchBeanException::new);
    }

    public <T> T getBean(String beanName, Class<T> beanType) {
        return context.entrySet().stream()
                .filter(x -> beanType.isAssignableFrom(x.getValue().getClass()))
                .map(x -> x.getKey().equals(beanName))
                .map(beanType::cast)
                .findFirst()
                .orElseThrow(NoSuchBeanException::new);
    }

    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return context.entrySet().stream()
                .filter(x -> beanType.isAssignableFrom(x.getValue().getClass()))
                .collect(toMap(Map.Entry::getKey, x -> beanType.cast(x.getValue())));
    }

    private static String resolveBeanName(Class<?> annotatedType) {
        String simpleName = annotatedType.getSimpleName();
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }
}
