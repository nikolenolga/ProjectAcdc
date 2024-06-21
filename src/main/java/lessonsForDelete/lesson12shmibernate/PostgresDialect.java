package lessonsForDelete.lesson12shmibernate;

import lessonsForDelete.lesson12shmibernate.converter.AbstractEntity;
import lessonsForDelete.lesson12shmibernate.engine.Dialect;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PostgresDialect implements Dialect {
    private static final String DELIMITER = ",\n";
    private static final String PREFIX = "(\n";
    private static final String SUFFIX = "\n)";
    private static final String COMMA = ", ";
    private static final String PRIMARY_KEY = "BIGSERIAL PRIMARY_KEY";
    private static final String UNIQUE = "UNIQUE ";
    private static final String NOT_NULL = "NOT NULL ";
    private static final String SPACE = " ";

    private static final Map<Class<?>, String> dbType = Map.of(
            int.class, "INT",
            Integer.class, "INT",
            long.class, "INT8",
            Long.class, "INT8",
            double.class, "FLOAT8",
            Double.class, "FLOAT8",
            String.class, "TEXT"
            //.............. etc
    );

    private static final Map<Class<?>, Function<String, Object>> readMap = Map.of(
            int.class, Integer::parseInt,
            Integer.class, Integer::valueOf,
            long.class, Long::parseLong,
            Long.class, Long::valueOf,
            double.class, Double::parseDouble,
            Double.class, Double::valueOf,
            String.class, String::toString
            //.............. etc
    );

    private String createTableSql;
    private String getAllSql;
    private String getSql;
    private String createSql;
    private String updateSql;
    private String deleteSql;

    @Override
    public String getCreateTableSql(String tableName, List<Field> fields) {
        return "";
    }

    @Override
    public String getGetAllSql(String tableName, List<Field> fields) {
        return "";
    }

    @Override
    public String getFindSql(String tableName, List<Field> fields, List<Field> whereFields) {
        return "";
    }

    @Override
    public String getGetByIdSql(String tableName, List<Field> fields) {
        return "";
    }

    @Override
    public String getCreateSql(String tableName, List<Field> fields) {
        return "";
    }

    @Override
    public String getUpdateSql(String tableName, List<Field> fields) {
        return "";
    }

    @Override
    public String getDeleteSql(String tableName, List<Field> fields) {
        return "";
    }

    @Override
    public <T extends AbstractEntity> void read(T entity, Field field, String valueString) {
        Class<?> type = field.getType();
        Object value = null;
        if(!type.isEnum()) {
            value = readMap.get(type).apply(valueString);
        } else {
            for(Object enumConstant : type.getEnumConstants()) {
                if(enumConstant.toString().equalsIgnoreCase(valueString)) {
                    value = enumConstant;
                    break;
                }
            }
        }

        try {
            field.setAccessible(true);
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
