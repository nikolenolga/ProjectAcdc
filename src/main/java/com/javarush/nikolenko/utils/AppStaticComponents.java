package com.javarush.nikolenko.utils;

import com.javarush.nikolenko.entity.User;

public class AppStaticComponents {
    public static final User ADMIN = new User("Admin", "admin", 100, "admin");
    public static final User ANONYMOUS = new User("Anonymous");

}
