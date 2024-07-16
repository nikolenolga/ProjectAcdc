package com.javarush.nikolenko.entity;

public interface AbstractComponent {
    Long getId();

    void setId(Long id);

    default String getImage() {
        return getClass().getSimpleName().toLowerCase() + "-" + getId();
    }
}
