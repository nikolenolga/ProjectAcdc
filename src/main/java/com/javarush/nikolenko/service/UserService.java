package com.javarush.nikolenko.service;

import com.javarush.nikolenko.dto.QuestTo;
import com.javarush.nikolenko.dto.Role;
import com.javarush.nikolenko.dto.UserTo;
import com.javarush.nikolenko.entity.Quest;
import com.javarush.nikolenko.entity.User;
import com.javarush.nikolenko.exception.QuestException;
import com.javarush.nikolenko.mapping.Dto;
import com.javarush.nikolenko.repository.UserRepository;
import com.javarush.nikolenko.utils.LoggerConstants;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public boolean updateUser(UserTo userTo, String name, String password) {
        if (!StringUtils.isAnyBlank(password) && (!userTo.getName().equals(name) || !userTo.getPassword().equals(password))) {
            userTo.setName(name);
            userTo.setPassword(password);
            return userRepository.update(Dto.MAPPER.from(userTo)).isPresent();
        }
        return false;
    }

    public Optional<UserTo> signIn(String currentLogin, String currentPassword, String currentName) {

        if (ObjectUtils.anyNull(currentPassword, currentLogin) || userRepository.userWithCurrentLoginExist(currentLogin)) {
            return Optional.empty();
        }

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
        Optional<User> optionalUser = userRepository.find(Dto.MAPPER.from(userTo));
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
        String login = getUniqueAnonymousLogin();
        User user = User.builder()
                .login(login)
                .role(Role.GUEST)
                .build();
        return userRepository.create(user).map(Dto.MAPPER::from);
    }

    private String getUniqueAnonymousLogin() {
        long count = userRepository.countAllUsers();
        long index = count + (int) (Math.random() * 100);

        int maxTries = 5;
        boolean userExist;
        while ((userExist = userRepository.userWithCurrentLoginExist("anonymous" + index)) && maxTries-- > 0) {
            index = count + (int) (Math.random() * 100);
        }

        if (userExist) {
            log.error(LoggerConstants.CAN_T_CREATE_ANONYMOUS_USER);
            throw new QuestException("Can't create anonymous user");
        }

        return "anonymous" + index;
    }
}
