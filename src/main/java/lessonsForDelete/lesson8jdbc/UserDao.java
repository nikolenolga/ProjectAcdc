package lessonsForDelete.lesson8jdbc;

import com.javarush.nikolenko.config.forDelete.CnnPool;
import com.javarush.nikolenko.entity.Role;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.exception.DaoException;
import com.javarush.nikolenko.repository.Repository;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class UserDao implements Repository<User> {

    public static final String SQL_GET_ALL = """
            SELECT "id", "login", "password", "role", "name"
            FROM "users"
            """;

    public static final String SQL_GET_BY_ID = """
            SELECT "id", "login", "password", "role", "name"
            FROM "users"
            WHERE id = ?
            """;

    public static final String SQL_CREATE = """
           INSERT INTO "users" ("login", "password", "role", "name")
           VALUES (?, ?, ?, ?)
           """;

    public static final String SQL_UPDATE = """
           UPDATE "users"
               SET login = ?,
                   password = ?,
                   role = ?,
                   name = ?
           WHERE id = ?;
           """;

    public static final String SQL_DELETE = """
            DELETE
            FROM "users"
            WHERE id = ?
            """;

    public static final String SQL_FIND = """
            SELECT "id", "login", "password", "role", "name"
            FROM "users"
            WHERE
            (? OR id = ?) AND
            (? OR login = ?) AND
            (? OR password = ?) AND
            (? OR role = ?) AND
            (? OR name = ?);
            """;

    public Stream<User> find(User user) {
        try(Connection connection = CnnPool.get();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND)
        ) {
            Long id = user.getId();
            preparedStatement.setBoolean(1, Objects.isNull(id));
            preparedStatement.setLong(2, Objects.nonNull(id)? id: 0L);

            String login = user.getLogin();
            preparedStatement.setBoolean(3, Objects.isNull(login));
            preparedStatement.setString(4, Objects.nonNull(login) ? login : "");

            String password = user.getPassword();
            preparedStatement.setBoolean(5, Objects.isNull(password));
            preparedStatement.setString(6, Objects.nonNull(password) ? password : "");

            Role role = user.getRole();
            preparedStatement.setBoolean(7, Objects.isNull(role));
            preparedStatement.setString(8, role != null ? role.toString() : "");

            String name = user.getName();
            preparedStatement.setBoolean(9, Objects.isNull(name));
            preparedStatement.setString(10, Objects.nonNull(name) ? name : "");

            //search users with not empty parameters and add to users stream
            return getUserStream(preparedStatement);

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static Stream<User> getUserStream(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        Stream.Builder<User> builder = Stream.builder();
        while (resultSet.next()) {
            Long newId = resultSet.getObject("id", Long.class);
            User newUser = User.builder()
                    .login(resultSet.getString("login"))
                    .password(resultSet.getString("password"))
                    .role(Role.valueOf(resultSet.getString("role")))
                    .name(resultSet.getString("name"))
                    .build();
            newUser.setId(newId);
            builder.add(newUser);
        }
        return builder.build();
    }

    @Override
    public void create(User user) {
        try(Connection connection = CnnPool.get();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.setString(4, user.getName());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();

            Long newId = generatedKeys.getLong(1);
            user.setId(newId);


        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public void update(User user) {
        try(Connection connection = CnnPool.get();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)
        ){
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.setString(4, user.getName());
            preparedStatement.setLong(5, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(User user) {
        try(Connection connection = CnnPool.get();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE)
        ){
            preparedStatement.setLong(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAll() {
        try(Connection connection = CnnPool.get();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL)
        ){
            return getUserStream(preparedStatement).toList();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> get(Long id) {
        try(Connection connection = CnnPool.get();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BY_ID)
        ){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                User user = User.builder()
                        .login(resultSet.getString("login"))
                        .password(resultSet.getString("password"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .name(resultSet.getString("name"))
                        .build();
                user.setId(id);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
