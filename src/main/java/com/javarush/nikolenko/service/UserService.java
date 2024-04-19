package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> create(User user) {
        if(ObjectUtils.allNotNull(user.getName(), user.getLogin(), user.getPassword())) {
            userRepository.create(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public Optional<User> update(User user) {
        if(ObjectUtils.allNotNull(user.getName(), user.getLogin(), user.getPassword())) {
            userRepository.update(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public Collection<User> getAll() {
        return userRepository.getAll();
    }

    public Optional<User> get(long id) {
        return userRepository.get(id);
    }

    public boolean userExist(String currentLogin) {
        return userRepository.userExist(currentLogin);
    }

    public Optional<User> getUser(String currentLogin, String currentPassword) {
        return userRepository.getUser(currentLogin, currentPassword);
    }

}
