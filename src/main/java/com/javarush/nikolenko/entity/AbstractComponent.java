package com.javarush.nikolenko.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractComponent {
    protected Long id;
    protected boolean img;

    protected AbstractComponent(long id) {
        this.id = id;
    }

    public void setImg(boolean img) {
        this.img = img;
    }

    public String getImage() {
        String imgPrefix = getClass().getSimpleName().toLowerCase() + "-";
        if (img) {
            return imgPrefix + id;
        }
        return imgPrefix + "default";
    }

}
