package com.javarush.nikolenko.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractComponent {
    public abstract Long getId();
    public abstract void setId(Long id);

    public String getImage() {
        return getClass().getSimpleName().toLowerCase() + "-";
    }
}
