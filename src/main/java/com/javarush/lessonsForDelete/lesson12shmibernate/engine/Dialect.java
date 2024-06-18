package com.javarush.lessonsForDelete.lesson12shmibernate.engine;

import com.javarush.lessonsForDelete.lesson12shmibernate.converter.AbstractEntity;

import java.lang.reflect.Field;
import java.util.List;

public interface Dialect {
    String getCreateTableSql(String tableName, List<Field> fields);
    String getGetAllSql(String tableName, List<Field> fields);
    String getFindSql(String tableName, List<Field> fields, List<Field> whereFields);
    String getGetByIdSql(String tableName, List<Field> fields);
    String getCreateSql(String tableName, List<Field> fields);
    String getUpdateSql(String tableName, List<Field> fields);
    String getDeleteSql(String tableName, List<Field> fields);

    <T extends AbstractEntity> void read(T entity, Field field, String valueString);
}
