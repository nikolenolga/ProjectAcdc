package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.dto.Role;
import com.javarush.nikolenko.dto.UserTo;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepositoryMock;
    private UserService userService;
    private UserTo savedUserTo;
    private UserTo userTo;
    private User user;
    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepositoryMock = mock(UserRepository.class);
        userService = new UserService(userRepositoryMock);

        user = User.builder()
                .login("testLogin")
                .password("testPassword")
                .role(Role.ADMIN)
                .name("TestName")
                .build();

        savedUser = User.builder()
                .id(7L)
                .login("testLogin")
                .password("testPassword")
                .role(Role.ADMIN)
                .name("TestName")
                .build();
        userTo = Dto.MAPPER.from(user);
        savedUserTo = Dto.MAPPER.from(savedUser);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenUserToAndNewData_whenUpdate_thenVerifyUpdate() {
        //given
        String name = "new name";
        String password = "new password";
        //when
        userService.update(savedUserTo, name, password);
        //then
        verify(userRepositoryMock).update(any(User.class));
    }

    @Test
    void givenUserToAndNewPassword_whenUpdate_thenVerifyUpdate() {
        //given
        String name = savedUserTo.getName();
        String password = "new password";
        //when
        userService.update(savedUserTo, name, password);
        //then
        verify(userRepositoryMock).update(any(User.class));
    }

    @Test
    void givenUserToAndNewName_whenUpdate_thenVerifyUpdate() {
        //given
        String name = "new name";
        String password = savedUserTo.getPassword();
        //when
        userService.update(savedUserTo, name, password);
        //then
        verify(userRepositoryMock).update(any(User.class));
    }

    @Test
    void givenUserToAndNewData_whenUpdate_thenUserToUpdated() {
        //given
        String name = "new name";
        String password = "new password";
        //when
        userService.update(savedUserTo, name, password);
        //then
        assertEquals(savedUserTo.getName(), name);
        assertEquals(savedUserTo.getPassword(), password);
    }

    @Test
    void givenData_whenSignIn_thenGetUserToWithData() {
        //given
        String currentLogin = "login";
        String currentPassword = "password";
        String currentName = "name";
        User tempUser = User.builder()
                .name(currentName)
                .login(currentLogin)
                .password(currentPassword)
                .role(Role.THE_USER)
                .build();

        when(userRepositoryMock.create(tempUser)).thenReturn(Optional.of(tempUser));

        //when
        Optional<UserTo> optionalTempUserTo = userService.signIn(currentLogin, currentPassword, currentName);

        //then
        assertTrue(optionalTempUserTo.isPresent());
        UserTo tempUserTo = optionalTempUserTo.get();
        assertEquals(tempUser.getName(), currentName);
        assertEquals(tempUser.getLogin(), currentLogin);
        assertEquals(tempUser.getPassword(), currentPassword);
        assertEquals(tempUser.getRole(), Role.THE_USER);
    }

    @Test
    void givenNullLogin_whenSignIn_thenGetEmptyOptional() {
        //given
        String currentLogin = null;
        String currentPassword = "password";
        String currentName = "name";

        //when
        Optional<UserTo> optionalTempUserTo = userService.signIn(currentLogin, currentPassword, currentName);

        //then
        assertTrue(optionalTempUserTo.isEmpty());
    }

    @Test
    void givenNullPassword_whenSignIn_thenGetEmptyOptional() {
        //given
        String currentLogin = "login";
        String currentPassword = null;
        String currentName = "name";

        //when
        Optional<UserTo> optionalTempUserTo = userService.signIn(currentLogin, currentPassword, currentName);

        //then
        assertTrue(optionalTempUserTo.isEmpty());
    }

    @Test
    void givenDataOfExistingUser_whenSignIn_thenGetUserToWithData() {
        //given
        String currentLogin = "login";
        String currentPassword = "password";
        String currentName = "name";
        when(userRepositoryMock.userWithCurrentLoginExist(currentLogin)).thenReturn(true);

        //when
        Optional<UserTo> optionalTempUserTo = userService.signIn(currentLogin, currentPassword, currentName);

        //then
        assertTrue(optionalTempUserTo.isEmpty());
    }

    @Test
    void givenLoginAndPassword_whenGetUser_thenVerifyGetUser() {
        //given
        String currentLogin = "testLogin";
        String currentPassword = "testPassword";
        //when
        userService.getUser(currentLogin, currentPassword);
        //then
        verify(userRepositoryMock).getUser(currentLogin, currentPassword);
    }

    @Test
    void givenLoginAndPassword_whenGetUser_thenGetUserToWithData() {
        //given
        String currentLogin = "testLogin";
        String currentPassword = "testPassword";
        when(userRepositoryMock.getUser(currentLogin, currentPassword)).thenReturn(Optional.of(savedUser));
        //when
        Optional<UserTo> tempUser = userService.getUser(currentLogin, currentPassword);
        //then
        assertTrue(tempUser.isPresent());
        assertEquals(savedUserTo, tempUser.get());
    }

    @Test
    void givenSavedUserTo_whenfindOrCreateUser_thenVerifyFindUser() {
        //given //when
        userService.findOrCreateUser(savedUserTo);
        //then
        verify(userRepositoryMock).find(any(User.class));
    }

    @Test
    void givenUserTo_whenfindOrCreateUser_thenVerifyFindUser() {
        //given //when
        userService.findOrCreateUser(userTo);
        //then
        verify(userRepositoryMock).find(any(User.class));
    }

    @Test
    void givenSavedUserTo_whenfindOrCreateUser_thenReturnSameUser() {
        //given
        when(userRepositoryMock.find(savedUser)).thenReturn(Stream.of(savedUser));
        // when
        Optional<UserTo> optionalUserTo = userService.findOrCreateUser(savedUserTo);
        //then
        assertTrue(optionalUserTo.isPresent());
        assertEquals(savedUserTo, optionalUserTo.get());

    }

    @Test
    void givenNewUserTo_whenfindOrCreateUser_thenVerifyCreateUser() {
        //given
        when(userRepositoryMock.find(user)).thenReturn(Stream.of());
        // when
        Optional<UserTo> optionalUserTo = userService.findOrCreateUser(userTo);
        //then
        verify(userRepositoryMock).create(any(User.class));

    }



    @Test
    void givenNewUserTo_whenfindOrCreateUser_thenReturnSavedUser() {
        //given
        when(userRepositoryMock.find(user)).thenReturn(Stream.of());
        when(userRepositoryMock.create(user)).thenReturn(Optional.of(savedUser));
        // when
        Optional<UserTo> optionalUserTo = userService.findOrCreateUser(userTo);
        //then
        assertTrue(optionalUserTo.isPresent());
        assertEquals(savedUserTo, optionalUserTo.get());

    }

    @Test
    void givenNotValidUserId_whenGetUserQuests_thenReturnEmptyCollection() {
        //given
        long userId = 0L;
        when(userRepositoryMock.get(userId)).thenReturn(Optional.empty());
        //when
        Collection<QuestTo> questTos = userService.getUserQuests(userId);
        //then
        assertTrue(questTos.isEmpty());
    }

    @Test
    void givenUserWithoutQuests_whenGetUserQuests_thenReturnEmptyCollection() {
        //given
        long userId = savedUserTo.getId();
        when(userRepositoryMock.get(userId)).thenReturn(Optional.of(savedUser));
        //when
        Collection<QuestTo> questTos = userService.getUserQuests(userId);
        //then
        assertTrue(questTos.isEmpty());
    }

    @Test
    void givenValidUserId_whenGetUserQuests_thenVerifyGet() {
        //given
        long userId = savedUserTo.getId();
        //when
        Collection<QuestTo> questTos = userService.getUserQuests(userId);
        //then
        verify(userRepositoryMock).get(userId);
    }

    @Test
    void givenUserWithQuests_whenGetUserQuests_thenReturnUserQuests() {
        //given
        Collection<QuestTo> expectedQuestTos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Quest quest = Quest.builder().name("Name " + i).build();
            savedUser.addQuest(quest);
            expectedQuestTos.add(Dto.MAPPER.from(quest));
        }

        long userId = savedUser.getId();
        when(userRepositoryMock.get(userId)).thenReturn(Optional.of(savedUser));
        //when
        Collection<QuestTo> actualQuestTos = userService.getUserQuests(userId);
        //then
        assertFalse(actualQuestTos.isEmpty());
        assertEquals(expectedQuestTos, actualQuestTos);
    }

    @Test
    void whenCreateAnonymousUser_thenVerifyCountAllUsers() {
        //given
        when(userRepositoryMock.countAllUsers()).thenReturn(5L);
        when(userRepositoryMock.userWithCurrentLoginExist(any(String.class))).thenReturn(false);
        when(userRepositoryMock.create(user)).thenReturn(Optional.of(savedUser));
        //when
        userService.createAnonymousUser();
        //then
        verify(userRepositoryMock).countAllUsers();
    }

    @Test
    void whenCreateAnonymousUser_thenVerifyUserExist() {
        //given
        when(userRepositoryMock.countAllUsers()).thenReturn(5L);
        when(userRepositoryMock.userWithCurrentLoginExist(any(String.class))).thenReturn(false);
        when(userRepositoryMock.create(user)).thenReturn(Optional.of(savedUser));
        //when
        userService.createAnonymousUser();
        //then
        verify(userRepositoryMock).userWithCurrentLoginExist(any(String.class));
    }

    @Test
    void whenCreateAnonymousUser_thenVerifyCreateUser() {
        //given
        when(userRepositoryMock.countAllUsers()).thenReturn(5L);
        when(userRepositoryMock.userWithCurrentLoginExist(any(String.class))).thenReturn(false);
        when(userRepositoryMock.create(user)).thenReturn(Optional.of(savedUser));
        //when
        userService.createAnonymousUser();
        //then
        verify(userRepositoryMock).create(any(User.class));
    }

    @Test
    void givenCantFindUnUsedLogin_whenCreateAnonymousUser_thenVerifyCountAllUsers() {
        //given
        when(userRepositoryMock.countAllUsers()).thenReturn(5L);
        when(userRepositoryMock.userWithCurrentLoginExist(any(String.class))).thenReturn(true);
        //when
        Optional<UserTo> optionalUserTo = userService.createAnonymousUser();
        //then
        assertTrue(optionalUserTo.isEmpty());
    }

    @Test
    void givenFreeLogin_whenCreateAnonymousUser_thenReturnNewUser() {
        //given
        when(userRepositoryMock.countAllUsers()).thenReturn(5L);
        when(userRepositoryMock.userWithCurrentLoginExist(any(String.class))).thenReturn(false);
        when(userRepositoryMock.create(user)).thenReturn(Optional.ofNullable(savedUser));
        //when
        Optional<UserTo> optionalUserTo = userService.createAnonymousUser();
        //then
        assertTrue(optionalUserTo.isPresent());
        assertEquals(savedUserTo, optionalUserTo.get());
    }
}