package com.javarush.khmelov.service;

import com.javarush.khmelov.entity.Role;
import com.javarush.khmelov.entity.User;
import com.javarush.khmelov.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Stream;

class UserServiceTest {

    private User user;
    private UserRepository userRepositoryMock;
    private UserService userService;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1L)
                .login("testLogin")
                .password("testPassword")
                .role(Role.USER)
                .build();
        userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.find(Mockito.any(User.class))).thenReturn(Stream.of(user));
        userService = new UserService(userRepositoryMock);
    }

    @Test
    void getById() {
        var optional = userService.get(1L);
        Assertions.assertEquals(user, optional.orElseThrow());
    }

    @Test
    void getByLoginAndPassword() {
        var optional = userService.get("login", "password");
        Assertions.assertEquals(user, optional.orElseThrow());
        Mockito.verify(userRepositoryMock).find(Mockito.any(User.class));
    }

    @Test
    void getAll() {
        Mockito.when(userRepositoryMock.getAll()).thenReturn(List.of(user, user));
        var users = userService.getAll();
        Assertions.assertTrue(users.contains(user));
        Assertions.assertEquals(2, users.size());
        Mockito.verify(userRepositoryMock).getAll();
    }

    @Test
    void update() {
        userService.update(user);
        Mockito.verify(userRepositoryMock).update(user);
    }

    @Test
    void create() {
        userService.create(user);
        Mockito.verify(userRepositoryMock).create(user);
    }

}