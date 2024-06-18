package com.javarush.lessonsForDelete.lesson7jdbc;

import com.javarush.nikolenko.config.Cnn;
import com.javarush.nikolenko.config.CnnConnector;

import java.sql.*;

public class JdbcDemo {
    public static final String ADD_USERS_SQL = """
            INSERT INTO users (id, login, password, role, name) VALUES
                    (DEFAULT, 'sergei', 'qwerty', 'THE_USER', 'Sergei'),
                    (DEFAULT, 'pavel', 'qwerty', 'THE_USER', 'Pavel'),
                    (DEFAULT, 'anna', 'qwerty', 'THE_USER', 'Anna');
            """;
    public static final String DELETE_USERS_MODE_ID4 = """
                DELETE FROM users WHERE id > 3;
            """;

    public static final String READ_ALL_VALUES = """
        SELECT id, login, password, role, name FROM users;
    """;

    public static void main(String[] args) throws SQLException {
        CnnConnector connector = new Cnn();
        try (Connection connection = connector.get(); Statement statement = connection.createStatement()) {
            System.out.println(connection);

            ResultSet resultSet = statement.executeQuery(READ_ALL_VALUES);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columns = metaData.getColumnCount();
            for (int i = 0; i < columns; i++) {
                String name = metaData.getColumnName(i+1);
                String type = metaData.getColumnTypeName(i+1);
                boolean isAuto = metaData.isAutoIncrement(i+1);

                System.out.printf("Column %s, type %s, AutoIncrement - %b%n", name, type, isAuto);
            }


            //resultSet ведет себя как курсор в бд
            while (resultSet.next()) {
                //long id = resultSet.getLong("id");
                Long id = resultSet.getObject("id", Long.class);
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                String name = resultSet.getString("name");

                //10 - это табы
                System.out.printf("%10d %10s %10s %10s %10s%n", id, login, password, role, name);
            }
            resultSet.close();

            int count_added = statement.executeUpdate(ADD_USERS_SQL, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            while (generatedKeys.next()) {
                Long id = generatedKeys.getObject("id", Long.class);
                System.out.println(id);
            }

            System.out.println(count_added);

            int count_deleted = statement.executeUpdate(DELETE_USERS_MODE_ID4, Statement.RETURN_GENERATED_KEYS);
            generatedKeys.close();

            ResultSet generatedKeysDeleted = statement.getGeneratedKeys();
            while(generatedKeysDeleted.next()) {
                Long id = generatedKeysDeleted.getObject("id", Long.class);
                String name = generatedKeysDeleted.getString("name");
                System.out.printf("User %s, with id=%d has been deleted%n", name, id);
            }

            System.out.println(count_deleted);
            generatedKeysDeleted.close();

            DatabaseMetaData dbMetaData = connection.getMetaData();
            dbMetaData.getSchemas();
            dbMetaData.getDriverName();
        }
    }
}
