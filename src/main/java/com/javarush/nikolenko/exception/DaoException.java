package com.javarush.nikolenko.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoException extends RuntimeException {
    public DaoException() {
        log.error("Exception in Dao");
    }
    public DaoException(String message) {
        super(message);
        log.error("Exception in Dao: {}", message);
    }
    public DaoException(String message, Throwable cause) {
        super(message, cause);
        log.error("Exception in Dao: {}, cause: {}", message, cause.toString());
    }
    public DaoException(Throwable cause) {
        super(cause);
        log.error("Exception in Dao, cause: {}", cause.toString());
    }
}
