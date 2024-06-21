package lessonsForDelete.lesson12shmibernate.engine;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StrategyNaming {
    private static final Map<String, String> cache = new HashMap<>();

    public static <T> String getTableName(Class<T> type) {
        return  type.isAnnotationPresent(Table.class) && type.getAnnotation(Table.class).name() != null && !type.getAnnotation(Table.class).name().isEmpty()
                ? type.getAnnotation(Table.class).name()
                : convertToSnakeStyle(type.getSimpleName());
    }

    public static String getFieldName(Field field) {
        return field.isAnnotationPresent(Column.class)
                ? field.getAnnotation(Column.class).name()
                : convertToSnakeStyle(field.getName());
    }

    public static String convertToSnakeStyleSimple(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = name.toCharArray();
        stringBuilder.append(chars[0]);
        for (int i = 1; i < chars.length; i++) {
            int charNum = chars[i];
            if(charNum > 100 && charNum < 133) stringBuilder.append("_");
            stringBuilder.append(chars[i]);
        }
        return stringBuilder.toString().toLowerCase();
    }

    public static String convertToSnakeStyle(String camelCase) {
        return cache.computeIfAbsent(camelCase, c -> c
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase());
    }
}
