package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.dto.Role;
import com.javarush.nikolenko.dto.UserTo;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

//    public Optional<UserTo> create(UserTo userTo) {
//        if (validateUser(userTo)) {
//            return userRepository.create(Dto.MAPPER.from(userTo)).map(Dto.MAPPER::from);
//        }
//        log.debug("User creation failed, user - {}", userTo);
//        return Optional.empty();
//    }

    public void update(UserTo userTo, String name, String password) {
        if (!userTo.getName().equals(name) || !userTo.getPassword().equals(password)) {
            userTo.setName(name);
            userTo.setPassword(password);
            userRepository.update(Dto.MAPPER.from(userTo));
        }
    }

//    private boolean validateUser(UserTo userTo) {
//        return userTo != null && ObjectUtils.allNotNull(userTo.getLogin());
//    }

//    public void delete(UserTo userTo) {
//        userRepository.delete(Dto.MAPPER.from(userTo));
//    }

//    public Collection<UserTo> getAll() {
//        return userRepository.getAll()
//                .stream()
//                .map(Dto.MAPPER::from)
//                .toList();
//    }

//    public Optional<UserTo> get(long id) {
//        return userRepository.get(id).map(Dto.MAPPER::from);
//    }

    public Optional<UserTo> signIn(String currentLogin, String currentPassword, String currentName) {

        if(ObjectUtils.anyNull(currentPassword, currentLogin) || userRepository.userWithCurrentLoginExist(currentLogin)) return Optional.empty();

        User user = User.builder()
                .name(currentName)
                .login(currentLogin)
                .password(currentPassword)
                .role(Role.THE_USER)
                .build();

        return userRepository.create(user).map(Dto.MAPPER::from);
    }

    public Optional<UserTo> getUser(String currentLogin, String currentPassword) {
        return userRepository.getUser(currentLogin, currentPassword)
                .map(Dto.MAPPER::from);
    }

    public Optional<UserTo> findOrCreateUser(UserTo userTo) {
        Optional<User> optionalUser = userRepository.find(Dto.MAPPER.from(userTo)).findFirst();
        if (optionalUser.isPresent()) {
            return optionalUser.map(Dto.MAPPER::from);
        } else {
            return userRepository.create(Dto.MAPPER.from(userTo)).map(Dto.MAPPER::from);
        }
    }

    public Collection<QuestTo> getUserQuests(long userId) {
        Collection<Quest> quests = userRepository.get(userId)
                .map(User::getQuests)
                .orElse(new ArrayList<>());

        return quests.stream()
                .map(Dto.MAPPER::from)
                .toList();
    }

    public Optional<UserTo> createAnonymousUser() {
        long count = userRepository.countAllUsers();
        long index = count + (int) (Math.random() * 100);

        int maxTries = 5;
        boolean userExist;
        while ((userExist = userRepository.userWithCurrentLoginExist("anonymous" + index)) && maxTries-- > 0) {
            index = count + (int) (Math.random() * 100);
        }

        if(userExist) return Optional.empty();

        User user = User.builder()
                .login("anonymous" + index)
                .role(Role.GUEST)
                .build();
        Optional<User> optionalUser = userRepository.create(user);
        if (optionalUser.isEmpty()) {
            throw new QuestException("Cannot create anonymous user");
        }

        return optionalUser.map(Dto.MAPPER::from);
    }
}
