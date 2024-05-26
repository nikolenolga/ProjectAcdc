package com.javarush.nikolenko.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractComponent {

    public String getImage() {
        return getClass().getSimpleName().toLowerCase() + "-";
    }
}
