package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        log.info("UserService created");
    }

    public Optional<User> create(User user) {
        if (user != null && ObjectUtils.allNotNull(user.getName(), user.getLogin(), user.getPassword())) {
            userRepository.create(user);
            return Optional.of(user);
        }
        log.debug("User creation failed, user - {}", user);
        return Optional.empty();
    }

    public Optional<User> update(User user) {
        if (user != null && ObjectUtils.allNotNull(user.getName(), user.getLogin(), user.getPassword())) {
            userRepository.update(user);
            return Optional.of(user);
        }
        log.debug("User updating failed, user - {}", user);
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
