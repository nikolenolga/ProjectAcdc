package com.javarush.nikolenko.exception;
//Добавить обработку ошибок https://for-each.dev/lessons/b/-servlet-exceptions
// https://www.baeldung.com/servlet-exceptions
//https://metanit.com/java/javaee/4.7.php
//https://stackoverflow.com/questions/7410414/how-to-grab-uncaught-exceptions-in-a-java-servlet-web-application
//https://forum.vingrad.ru/forum/topic-163690.html

public class QuestException extends RuntimeException {

    public QuestException(String message) {
        super(message);
    }

    public QuestException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestException(Throwable cause) {
        super(cause);
    }

}
