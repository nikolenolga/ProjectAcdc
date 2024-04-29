package com.javarush.nikolenko.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractComponent {
    protected Long id;

    protected AbstractComponent(long id) {
        this.id = id;
    }

    public String getImage() {
        return getClass().getSimpleName().toLowerCase() + "-" + id;
    }
}
