package com.javarush.nikolenko.repository;

import com.javarush.nikolenko.ContainerIT;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.Role;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.config.SessionCreater;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryIT extends ContainerIT {
    private static final SessionCreater sessionCreater = NanoSpring.find(SessionCreater.class);
    private static final UserRepository userRepository = new UserRepository(sessionCreater);
    private User testUser;

    @BeforeEach
    void setUp() {
        sessionCreater.beginTransactional();
        testUser = User.builder()
                .login("testLogin")
                .password("testPassword")
                .role(Role.ADMIN)
                .name("TestName")
                .build();
        userRepository.create(testUser);
    }

    @Test
    void givenCreatedTestUser_whenGetId_thenIdNotNullAndNot0L() {
        assertTrue(testUser.getId() != null && testUser.getId() != 0L);
    }

    @Test
    void givenCreatedTestUser_whenGetUserWithId_thenEqualUser() {
        Optional<User> optionalUser = userRepository.get(testUser.getId());
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertEquals(testUser, user);
    }

    @Test
    void givenCreatedTestUser_whenSetNewLogin_thenGetUserWithUpdatedLogin() {
        testUser.setLogin("newLogin");
        userRepository.update(testUser);
        Optional<User> optionalUser = userRepository.get(testUser.getId());
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertEquals(testUser, user);
    }

    @Test
    void givenCreatedTestUser_whenFindUserWithThisNameAndLogin_thenGetEqualUser() {
        User user = User.builder()
                .name(testUser.getName())
                .login(testUser.getLogin())
                .build();
        Optional<User> optionalUser = userRepository.find(user);
        assertTrue(optionalUser.isPresent());
        assertEquals(testUser, optionalUser.get());
    }

    @Test
    void givenCreatedTestUser_whenDeleteUser_thenGetEmptyOptionalUser() {
        User testUserForDelete = User.builder()
                .login("testLoginDelete")
                .password("testPassword")
                .role(Role.ADMIN)
                .name("TestName")
                .build();
        userRepository.create(testUserForDelete);

        userRepository.delete(testUserForDelete);
        Optional<User> optionalUser = userRepository.get(testUserForDelete.getId());
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    void givenCreatedTestUser_whenDeleteUserById_thenGetEmptyOptionalUser() {
        User testUserForDelete = User.builder()
                .login("testLoginDelete")
                .password("testPassword")
                .role(Role.ADMIN)
                .name("TestName")
                .build();
        userRepository.create(testUserForDelete);

        userRepository.delete(testUserForDelete.getId());
        Optional<User> optionalUser = userRepository.get(testUserForDelete.getId());
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    void givenCreatedUsers_whenGetAll_thenGetNotEmptyCollection() {
        for (int i = 16; i < 30; i++) {
            userRepository.create(User.builder().login("newTestLogin" + i).name("newTestNeme" + i).password("newTestPassword").role(Role.THE_USER).build());
        }
        Collection<User> users = userRepository.getAll();
        assertFalse(users.isEmpty());
    }

    @Test
    void givenCountGetAllUsers_whenAddNewUsersAndCountAgain_thenDeltaIsExpected() {
        //given
        int expected = 15;
        int countBefore = userRepository.getAll().size();
        List<User> users = new ArrayList<>();

        //when
        for (int i = 0; i < expected; i++) {
            Optional<User> optionalUser = userRepository.create(User.builder()
                    .login("newTestLogin" + i)
                    .name("newTestNeme" + i)
                    .password("newTestPassword")
                    .role(Role.THE_USER)
                    .build());
            optionalUser.ifPresent(users::add);
        }
        int countAfter = userRepository.getAll().size();
        users.forEach(userRepository::delete);

        //then
        int actual = countAfter - countBefore;
        assertEquals(expected, actual);
    }

    @Test
    void givenCreatedTestUser_whenCheckUserExistWithTestUserLogin_thenTrue() {
        boolean expected = userRepository.userWithCurrentLoginExist(testUser.getLogin());
        assertTrue(expected);
    }

    @Test
    void whenCreateUser_thenCheckUserExistTrue() {
        List<String> logins = new ArrayList<>();
        for (int i = 31; i < 45; i++) {
            userRepository.create(User.builder()
                    .login("newTestLogin" + i)
                    .name("newTestNeme" + i)
                    .password("newTestPassword" + i)
                    .role(Role.THE_USER)
                    .build()
            );
            logins.add("newTestLogin" + i);
        }

        logins.forEach(login -> assertTrue(userRepository.userWithCurrentLoginExist(login)));
    }

    @Test
    void givenCreatedTestUser_whenGetUserWithLoginAndPassword_thenGetUserEqualToTestUser() {
        Optional<User> optionalUser = userRepository.getUser(testUser.getLogin(), testUser.getPassword());
        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get(), testUser);
    }

    @Test
    void givenCountedUsers_whenCreateOneUser_thenCountAllUsersDeltaIsOne() {
        long countBefore = userRepository.countAllUsers();
        User user = User.builder()
                .login("newTestLogin")
                .password("newTestPassword")
                .role(Role.THE_USER)
                .name("NewTestName")
                .build();
        userRepository.create(user);

        long countAfter = userRepository.countAllUsers();

        assertEquals(1L, countAfter - countBefore);
    }

    @AfterEach
    void tearDown() {
        userRepository.delete(testUser);
        sessionCreater.endTransactional();
    }
}