package com.javarush.lessonsForDelete.lesson8jdbc;

import com.javarush.nikolenko.config.forDelete.CnnPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcPrepStatDemo {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                try(Connection connection = CnnPool.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(UserDao.SQL_GET_BY_ID)
                ){
                    preparedStatement.setLong(1, 1L);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString("login"));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }
    }
}
