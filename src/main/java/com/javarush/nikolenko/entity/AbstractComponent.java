package com.javarush.nikolenko.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.beans.Transient;

public interface AbstractComponent {
    Long getId();
    void setId(Long id);

    @Transient
    default String getImage() {
        return getClass().getSimpleName().toLowerCase() + "-" + getId();
    }
}
