package com.javarush.nikolenko.service;

import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        userRepository.create(user);
    }

    public void update(User user) {
        userRepository.update(user);
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

//    public long getCurrentQuestionId(long id) {
//        return get(id).map(User::getFirstQuestionId).orElse(0L);
//    }

}
