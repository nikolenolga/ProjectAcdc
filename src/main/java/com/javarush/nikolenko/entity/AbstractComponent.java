package com.javarush.nikolenko.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public interface AbstractComponent {
    Long getId();
    void setId(Long id);

    default String getImage() {
        return getClass().getSimpleName().toLowerCase() + "-" + getId();
    }
}
