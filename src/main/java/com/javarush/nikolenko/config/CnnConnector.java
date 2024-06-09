package com.javarush.nikolenko.config;

import java.sql.Connection;

public interface CnnConnector {
    Connection get();
}
