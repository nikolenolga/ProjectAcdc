package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.config.ApplicationProperties;
import com.javarush.nikolenko.entity.Role;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.config.SessionCreater;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Stream;

class UserRepositoryTest {
    private final UserRepository userRepository = new UserRepository(new SessionCreater(new ApplicationProperties()));
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .login("testLodin")
                .password("testPassword")
                .role(Role.ADMIN)
                .name("TestName")
                .build();
        userRepository.create(testUser);
    }

    @Test
    void get() {
        Optional<User> optionalUser = userRepository.get(testUser.getId());
        Assertions.assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        Assertions.assertEquals(testUser, user);
    }

    @Test
    void update() {
        testUser.setLogin("newLogin");
        userRepository.update(testUser);
        Optional<User> optionalUser = userRepository.get(testUser.getId());
        Assertions.assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        Assertions.assertEquals(testUser, user);
    }

    @Test
    void find() {
        User user = User.builder()
                .login(testUser.getLogin())
                .name(testUser.getName())
                .build();
        Stream<User> userStream = userRepository.find(user);
        Assertions.assertEquals(testUser, userStream.findFirst().orElseThrow());
    }

    @Test
    void delete() {
        userRepository.delete(testUser);
        Optional<User> optionalUser = userRepository.get(testUser.getId());
        Assertions.assertTrue(optionalUser.isEmpty());
    }


    @AfterEach
    void tearDown() {
        userRepository.delete(testUser);
    }
}