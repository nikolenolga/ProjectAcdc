package com.javarush.nikolenko.lesson12shmibernate.engine;

import java.sql.Connection;

public interface CnnConnector {
    Connection get();
}
