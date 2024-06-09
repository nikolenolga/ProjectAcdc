package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.config.SessionCreater;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class UserRepository extends BaseRepository<User> {
    public UserRepository(SessionCreater sessionCreater) {
        super(sessionCreater, User.class);
    }

    public boolean userExist(String currentLogin) {
        return getAll()
                .stream()
                .map(User::getLogin)
                .anyMatch(login -> login.equals(currentLogin));
    }

    public Optional<User> getUser(String currentLogin, String currentPassword) {
        return getAll()
                .stream()
                .filter(user -> StringUtils.equals(user.getLogin(), currentLogin))
                .filter(user -> StringUtils.equals(user.getPassword(), currentPassword))
                .findFirst();
    }
}
