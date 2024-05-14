package com.javarush.nikolenko.lesson7jdbc;

import java.sql.Connection;

public interface CnnConnector {
    Connection get();
}
