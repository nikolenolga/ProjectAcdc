package com.javarush.nikolenko.lesson9Hibernate;

import com.javarush.nikolenko.config.ApplicationProperties;
import com.javarush.nikolenko.config.SessionCreater;
import com.javarush.nikolenko.entity.Role;
import com.javarush.nikolenko.entity.User;
import lessonsForDelete.lesson9Hibernate.UserDbDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserDbDaoTest {
    private Session session;
    private SessionCreater sessionCreater;
    private UserDbDao userDbDao;

    @BeforeEach
    void setUp() {
        sessionCreater = new SessionCreater(new ApplicationProperties());
        session = sessionCreater.getSession();
        userDbDao = new UserDbDao(sessionCreater);
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
        userDbDao.create(tempUser);
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
        userDbDao.create(tempUser);
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
        User tempUser = User.builder()
                .id(0L)
                .name("Olga")
                .login("olga")
                .password("123")
                .role(Role.THE_USER)
                .build();
        userDbDao.create(tempUser);

        //when then
        tempUser.setName("newValue");
        userDbDao.update(tempUser);
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
        User tempUser = User.builder()
                .id(0L)
                .name("Olga")
                .login("olga")
                .password("123")
                .role(Role.THE_USER)
                .build();
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

    @Test
    @DisplayName("When find by id then get user id=1, role=ADMIN")
    void find() {
        Transaction transaction = session.beginTransaction();
        User pattern = User.builder().role(Role.ADMIN).build();
        Stream<User> userStream = userDbDao.find(pattern);
        userStream.forEach(System.out::println);
        transaction.commit();
    }

    @AfterEach
    void close() throws IOException {
        sessionCreater.close();
    }
}