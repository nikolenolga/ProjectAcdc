package com.javarush.lessonsForDelete.lesson12shmibernate.engine;

import java.sql.Connection;

public interface CnnConnector {
    Connection get();
}
