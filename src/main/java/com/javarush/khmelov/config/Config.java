package com.javarush.khmelov.config;

import com.javarush.khmelov.entity.Role;
import com.javarush.khmelov.entity.User;
import com.javarush.khmelov.service.UserService;

public class Config {

    private final UserService userService;

    public Config(UserService userService) {
        this.userService = userService;
    }

    public void fillEmptyRepository() {
        if (userService.get(1L).isEmpty()) {
            User admin = new User(-1L, "Carl", "admin", Role.ADMIN);
            userService.create(admin);
            userService.create(new User(-1L, "Alisa", "qwerty", Role.USER));
            userService.create(new User(-1L, "Bob", "123", Role.GUEST));
        }
    }
}
