package com.javarush.nikolenko.lesson12shmibernate.converter;

import jakarta.persistence.AttributeConverter;

import java.sql.Blob;

public class DemoConverter implements AttributeConverter<Password, String> {
    @Override
    public String convertToDatabaseColumn(Password password) {
        return password.value;
    }

    @Override
    public Password convertToEntityAttribute(String value) {
        Password password = new Password();
        password.value = value;
        return password;
    }
}
