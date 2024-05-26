package com.javarush.nikolenko.lesson9Hibernate;

import com.javarush.nikolenko.entity.Role;
import com.javarush.nikolenko.entity.User;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDbDaoTest {
    private SessionCreater sessionCreater = new SessionCreater();
    private UserDbDao userDbDao = new UserDbDao(sessionCreater);


    @Test
    @DisplayName("When create tempUser then no exeption")
    void givenUser_whenCreate_thenNoException() {
        User tempUser = User.builder()
                .login("newLogin")
                .password("newPassword")
                .role(Role.THE_USER)
                .name("newName")
                .build();
        userDbDao.create(tempUser);
    }

    @Test
    @DisplayName("Given tempUser with id-0 when create then tempUser id not 0")
    void givenUserWith0id_whenCreate_thenIdnot0() {
        //given
        Long expected = 0L;
        User tempUser = new User(expected, "newName", "newLogin", "newPassword", Role.THE_USER);
        //when
        userDbDao.create(tempUser);
        Long actual = tempUser.getId();
        //then
        assertNotEquals(expected, actual);
    }

    @Test
    void givenNewName_whenUpdate_thenGetNewName() {
        //given
        User tempUser = new User(0L, "Olga", "olga", "123", Role.THE_USER);
        userDbDao.create(tempUser);

        //when
        String expected = "newValue";
        tempUser.setName(expected);
        userDbDao.update(tempUser);

        //then
        assertEquals(expected, tempUser.getName());
    }

    @Test
    @DisplayName("When update tempUser then no exeption")
    void givenUser_whenUpdate_thenNoException() {
        //given
        User tempUser = new User(0L, "Olga", "olga", "123", Role.THE_USER);
        userDbDao.create(tempUser);

        //when then
        tempUser.setName("newValue");
        userDbDao.update(tempUser);
    }

    @Test
    void givenCreatedUser_whenDelete_thenGetEmptyOptionalUser() {
        //given
        User tempUser = new User(0L, "Olga", "olga", "123", Role.THE_USER);
        userDbDao.create(tempUser);
        long expectedId = tempUser.getId();

        //when
        userDbDao.delete(tempUser);
        Optional<User> optionalUser = userDbDao.get(expectedId);
        //then
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    @DisplayName("When delete tempUser then no exeption")
    void givenUser_whenDelete_thenNoException() {
        //given
        User tempUser = new User(0L, "Olga", "olga", "123", Role.THE_USER);
        userDbDao.create(tempUser);
        long expectedId = tempUser.getId();

        //when then
        userDbDao.delete(tempUser);
    }


    @Test
    void givenIdOne_whenGet_thenOptionalUserIsPresent() {
        //given
        long expectedId = 1L;
        Role expectedRole = Role.ADMIN;
        //when
        Optional<User> optionalUser = userDbDao.get(expectedId);
        //then
        assertTrue(optionalUser.isPresent());
        User tempUser = optionalUser.get();
        assertEquals(expectedId, tempUser.getId());
        assertEquals(expectedRole, tempUser.getRole());

    }

    @Test
    void givenIdOne_whenGet_thenUserIdIs0RoleIsAdmin() {
        //given
        long expectedId = 1L;
        Role expectedRole = Role.ADMIN;
        //when
        User tempUser = userDbDao.get(expectedId).get();
        //then
        assertEquals(expectedId, tempUser.getId());
        assertEquals(expectedRole, tempUser.getRole());

    }

    @AfterEach
    void close() throws IOException {
        sessionCreater.close();
    }
}