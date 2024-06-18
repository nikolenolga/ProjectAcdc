package com.javarush.nikolenko.lesson8jdbc;

import com.javarush.nikolenko.entity.Role;
import com.javarush.nikolenko.entity.User;
import com.javarush.lessonsForDelete.lesson8jdbc.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
    }

    public static Stream<Arguments> getSamplePatternForSearch() {

        return Stream.of(
                Arguments.of(User.builder().login("admin").password("admin").build(), 1),
                Arguments.of(User.builder().login("olga").password("badpass").build(), 0),
                Arguments.of(User.builder().login("admin").build(), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("getSamplePatternForSearch")
    @DisplayName("Check by not null fields")
    void find(User user, int expected) {
        long actual = userDao.find(user).count();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When create tempUser then no exeption")
    void givenUser_whenCreate_thenNoException() {
        User tempUser = User.builder()
                .login("newLogin")
                .password("newPassword")
                .role(Role.THE_USER)
                .name("newName")
                .build();
        userDao.create(tempUser);
    }

    @Test
    @DisplayName("When find all users result contains admin")
    void getAll() {
        List<User> users = userDao.getAll();
        assertTrue(users.stream().anyMatch(user -> user.getRole() == Role.ADMIN));
    }

    @Test
    @DisplayName("Given tempUser with id-0 when create then tempUser id not 0")
    void givenUserWith0id_whenCreate_thenIdnot0() {
        //given
        Long expected = 0L;
        User tempUser = User.builder()
                .id(expected)
                .name("newName")
                .login("newLogin")
                .password("newPassword")
                .role(Role.THE_USER)
                .build();
        //when
        userDao.create(tempUser);
        Long actual = tempUser.getId();
        //then
        assertNotEquals(expected, actual);
    }

    @Test
    void givenNewName_whenUpdate_thenGetNewName() {
        //given
        User tempUser = User.builder()
                .id(0L)
                .name("Olga")
                .login("olga")
                .password("123")
                .role(Role.THE_USER)
                .build();
        userDao.create(tempUser);

        //when
        String expected = "newValue";
        tempUser.setName(expected);
        userDao.update(tempUser);

        //then
        assertEquals(expected, tempUser.getName());
    }

    @Test
    @DisplayName("When update tempUser then no exeption")
    void givenUser_whenUpdate_thenNoException() {
        //given
        User tempUser = User.builder()
                .id(0L)
                .name("Olga")
                .login("olga")
                .password("123")
                .role(Role.THE_USER)
                .build();
        userDao.create(tempUser);

        //when then
        tempUser.setName("newValue");
        userDao.update(tempUser);
    }

    @Test
    void givenCreatedUser_whenDelete_thenGetEmptyOptionalUser() {
        //given
        User tempUser = User.builder()
                .id(0L)
                .name("Olga")
                .login("olga")
                .password("123")
                .role(Role.THE_USER)
                .build();
        userDao.create(tempUser);
        long expectedId = tempUser.getId();

        //when
        userDao.delete(tempUser);
        Optional<User> optionalUser = userDao.get(expectedId);
        //then
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    @DisplayName("When delete tempUser then no exeption")
    void givenUser_whenDelete_thenNoException() {
        //given
        User tempUser = User.builder()
                .id(0L)
                .name("Olga")
                .login("olga")
                .password("123")
                .role(Role.THE_USER)
                .build();
        userDao.create(tempUser);
        long expectedId = tempUser.getId();

        //when then
        userDao.delete(tempUser);
    }

    @Test
    void whenGetAll_thenCountNotEmpty() {
        //given when
        long count = userDao.getAll().size();
        //then
        assertTrue(count > 0);
    }

    @Test
    void givenIdOne_whenGet_thenOptionalUserIsPresent() {
        //given
        long expectedId = 1L;
        Role expectedRole = Role.ADMIN;
        //when
        Optional<User> optionalUser = userDao.get(expectedId);
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
        User tempUser = userDao.get(expectedId).get();
        //then
        assertEquals(expectedId, tempUser.getId());
        assertEquals(expectedRole, tempUser.getRole());

    }
}