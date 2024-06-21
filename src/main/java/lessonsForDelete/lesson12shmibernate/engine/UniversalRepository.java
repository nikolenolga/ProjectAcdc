package lessonsForDelete.lesson12shmibernate.engine;

import lessonsForDelete.lesson12shmibernate.converter.AbstractEntity;
import jakarta.persistence.*;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Stream;


public class UniversalRepository<T extends AbstractEntity> implements Repository<T> {
    private final Class<T> type;
    private final Dialect dialect;

    private final List<Field> fields;
    private final String tableName;

    public UniversalRepository(Class<T> type, Dialect dialect) {
        this.type = type;
        this.dialect = dialect;
        if(type.isAnnotationPresent(Entity.class)){
            fields = Arrays.stream(type.getDeclaredFields())
                    .sorted(Comparator.comparingInt(f -> (f.isAnnotationPresent(Id.class) ? 0 : 1)))
                    .filter(f -> !f.isAnnotationPresent(Transient.class))
                    .filter(f -> !f.isAnnotationPresent(ManyToOne.class))
                    .filter(f -> !f.isAnnotationPresent(OneToMany.class))
                    .filter(f -> !f.isAnnotationPresent(OneToOne.class))
                    .filter(f -> !f.isAnnotationPresent(ManyToMany.class))
                    .toList();
            tableName = StrategyNaming.getTableName(type);
            createTableIfNotExists();
        } else {
            throw new RuntimeException("incorrect class");
        }
    }

    @SneakyThrows
    private void createTableIfNotExists() {
        String sql = dialect.getCreateTableSql(tableName, fields);
        System.out.println(sql);
        try (Connection connection = CnnPool.get();
            Statement statement = connection.createStatement()){

            statement.executeUpdate(sql);
        }
    }

    @Override
    public Collection<T> getAll() {
        return List.of();
    }

    @SneakyThrows
    @Override
    public Stream<T> find(T pattern) {
        List<Field> whereField = fields.stream()
                .filter(f -> containsValue(f, pattern))
                .toList();
        String findSql = dialect.getFindSql(tableName, fields, whereField);
        try(Connection connection = CnnPool.get();
            PreparedStatement preparedStatement = connection.prepareStatement(findSql)
        ){
            fill(pattern, preparedStatement, whereField);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            return readRows(resultSet);
        }
    }

    @SneakyThrows
    @Override
    public T get(Long id) {
        String sql = dialect.getGetByIdSql(tableName, fields);
        System.out.println(sql);
        try (Connection connection = CnnPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            T entity = type.getConstructor().newInstance();
            entity.setId(id);
            setId(entity, preparedStatement, 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            return readRows(resultSet).findFirst().orElse(null);
        }
    }

    @Override
    public void create(T entity) {

    }

    @Override
    public void update(T entity) {

    }

    @Override
    public void delete(T entity) {

    }

    @SneakyThrows
    private Stream<T> readRows(ResultSet resulSet) {
        List<T> list = new ArrayList<>();
        while (resulSet.next()) {
            T entity = type.getConstructor().newInstance();
            for(Field field : fields) {
                String fieldName = StrategyNaming.getFieldName(field);
                String valueString = resulSet.getString(fieldName);
                dialect.read(entity, field, valueString);
            }
            list.add(entity);
        }
        return list.stream();
    }

    private <T> void setId(T entity, PreparedStatement preparedStatement, Integer pos) {
        try {
            Field idField = fields.get(0);
            Object value = getValue(entity, idField);
            preparedStatement.setObject(pos, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void fill(T entity, PreparedStatement preparedStatement, List<Field> whereField) {
        Stream.iterate(0, i -> ++i)
                .limit(whereField.size())
                .forEach(i -> {
                    Field field = whereField.get(i);
                    field.setAccessible(true);
                    try {
                        Object value = getValue(entity, field);
                        preparedStatement.setObject(i + 1, value);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private <T> void fill(T entity, PreparedStatement preparedStatement) {
        List<Field> dataFields = fields.subList(1, fields.size());
        fill(entity, preparedStatement, dataFields);
    }

    private <T> Object getValue(T entity, Field field) {
        field.setAccessible(true);
        try {
            Object value = field.get(entity);
            if(field.getType().isEnum()) {
                value = value.toString();
            }
            return value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean containsValue(Field f, Object entity) {
        f.setAccessible(true);
        try {
            return f.get(entity) != null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
