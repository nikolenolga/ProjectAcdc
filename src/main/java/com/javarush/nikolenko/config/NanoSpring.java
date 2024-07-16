package com.javarush.nikolenko.config;

import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.utils.LoggerConstants;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;

@Slf4j
public class NanoSpring {
    private static final Map<Class<?>, Object> components = new ConcurrentHashMap<>();

    private static final List<Class<?>> beanDefinitions = new ArrayList<>();
    private static final String CLASSES = File.separator + "classes" + File.separator;
    private static final String EXT = ".class";
    private static final String DOT = ".";
    private static final String COMMA = ",";
    private static final String GENERIC_START = "<";
    private static final String GENERIC_END = ">";
    private static final String EMPTY = "";
    public static final String[] NANOSPRING_INIT_EXCLUDES = {"Controller", "Servlet", "Filter", "controller", "servlet", "filter"};

    @SneakyThrows
    public static <T> T find(Class<T> clazz) {
        if (beanDefinitions.isEmpty()) {
            init();
        }
        Object component = components.get(clazz);
        if (component == null) {
            Constructor<?> constructor = clazz.getConstructors()[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Type[] genericParameterTypes = constructor.getGenericParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];
            try {
                for (int i = 0; i < parameters.length; i++) {
                    Class<?> impl = findImpl(parameterTypes[i], genericParameterTypes[i]);
                    parameters[i] = find(impl);
                }
            } catch (IndexOutOfBoundsException e) {
                log.error(LoggerConstants.INDEX_OUT_OF_BOUNDS_EXCEPTION_IN, clazz.getSimpleName());
                throw new QuestException(clazz.getSimpleName(), e);
            }
            Object newInstance = checkTransactional(clazz)
                    ? constructProxyInstance(clazz, parameterTypes, parameters)
                    : constructor.newInstance(parameters);
            components.put(clazz, newInstance);
            log.debug(LoggerConstants.NANO_SPRING_CREATED_NEW_INSTANCE_OF, clazz);
        }

        return (T) components.get(clazz);
    }

    @SneakyThrows
    private static void init() {
        URL resource = NanoSpring.class.getResource("NanoSpring.class");
        URI uri = Objects.requireNonNull(resource).toURI();
        Path appRoot = Path.of(uri).getParent().getParent();
        scanPackages(appRoot, NANOSPRING_INIT_EXCLUDES);
    }

    public static void scanPackages(Path appPackage, String... excludes) {
        try (Stream<Path> walk = Files.walk(appPackage)) {
            List<String> names = walk.map(Path::toString)
                    .filter(o -> o.endsWith(EXT))
                    .filter(o -> Arrays.stream(excludes).noneMatch(o::contains))
                    .map(s -> s.substring(s.indexOf(CLASSES) + CLASSES.length()))
                    .map(s -> s.replace(EXT, EMPTY))
                    .map(s -> s.replace(File.separator, DOT))
                    .toList();
            for (String name : names) {
                beanDefinitions.add(Class.forName(name));
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error(LoggerConstants.NANO_SPRING_SCAN_PACKAGES_CAUSED_EXEPTION_BEAN_IS_NOT_IN_EXCLUDES_LIST, e.getMessage(), excludes);
            throw new QuestException(e);
        }
    }

    private static Class<?> findImpl(Class<?> aClass, Type type) {
        for (Class<?> beanDefinition : beanDefinitions) {
            boolean assignable = aClass.isAssignableFrom(beanDefinition);
            boolean nonGeneric = beanDefinition.getTypeParameters().length == 0;
            boolean nonInterface = !beanDefinition.isInterface();
            boolean nonAbstract = !Modifier.isAbstract(beanDefinition.getModifiers());
            boolean checkGenerics = checkGenerics(type, beanDefinition);
            if (assignable & nonGeneric & nonInterface & nonAbstract && checkGenerics) {
                return beanDefinition;
            }
        }
        log.info(LoggerConstants.NOT_FOUND_IMPL_FOR_TYPE, aClass, type);
        throw new QuestException("Not found impl for %s (type=%s)".formatted(aClass, type));
    }

    private static boolean checkGenerics(Type type, Class<?> impl) {
        var typeContractGeneric = NanoSpring.getContractGeneric(type);
        return Objects.nonNull(impl) &&
                Stream.iterate(impl, Objects::nonNull, (Class<?> c) -> c.getSuperclass())
                        .flatMap(c -> Stream.concat(
                                Stream.of(c.getGenericSuperclass()),
                                Stream.of(c.getGenericInterfaces())))
                        .filter(Objects::nonNull)
                        .map(NanoSpring::getContractGeneric)
                        .anyMatch(typeContractGeneric::equals);
    }

    private static List<? extends Class<?>> getContractGeneric(Type type) {
        var typeName = type.getTypeName();
        return !typeName.contains("<")
                ? List.of()
                : Arrays.stream(typeName
                        .replaceFirst(".+<", EMPTY)
                        .replace(">", EMPTY)
                        .split(COMMA))
                .map(NanoSpring::getaClassOrNull)
                .toList();
    }

    private static Class<?> getaClassOrNull(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error(LoggerConstants.NOT_FOUND, className, e);
            return null;
        }
    }

    //add proxy
    private static <T> boolean checkTransactional(Class<T> type) {
        return type.isAnnotationPresent(Transactional.class)
                || Arrays.stream(type.getMethods())
                .anyMatch(method -> method.isAnnotationPresent(Transactional.class));
    }

    @SneakyThrows
    private static Object constructProxyInstance(Class<?> type, Class<?>[] parameterTypes, Object[] parameters) {
        Class<?> proxy = new ByteBuddy()
                .subclass(type)
                .method(isDeclaredBy(ElementMatchers.isAnnotatedWith(Transactional.class))
                        .or(ElementMatchers.isAnnotatedWith(Transactional.class)))
                .intercept(MethodDelegation.to(Interceptor.class))
                .make()
                .load(type.getClassLoader())
                .getLoaded();
        Constructor<?> constructor = proxy.getConstructor(parameterTypes);
        return constructor.newInstance(parameters);
    }


    public static class Interceptor {
        @RuntimeType
        public static Object intercept(@This Object self,
                                       @Origin Method method,
                                       @AllArguments Object[] args,
                                       @SuperMethod Method superMethod) throws Throwable {
            SessionCreater sessionCreater = find(SessionCreater.class);
            sessionCreater.beginTransactional();
            try {
                return superMethod.invoke(self, args);
            } finally {
                sessionCreater.endTransactional();
            }
        }
    }
}
