package com.javarush.nikolenko.config;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServiceLocator {
    private static final Map<Class<?>, Object> servicesWithComponents = new ConcurrentHashMap<>();

    public static <T> T getService(Class<T> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object service = servicesWithComponents.get(clazz);
        if (service == null) {
            Constructor<?> constructor = clazz.getConstructors()[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = ServiceLocator.getService(parameterTypes[i]);
            }
            Object newInstance = constructor.newInstance(parameters);
            servicesWithComponents.put(clazz, newInstance);
            log.info("ServiceLocator created new instance of {}", clazz);
        }

        return (T) servicesWithComponents.get(clazz);
    }

}
