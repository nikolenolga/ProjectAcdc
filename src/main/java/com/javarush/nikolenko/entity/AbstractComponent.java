package com.javarush.nikolenko.entity;

public abstract class AbstractComponent {
    protected long id;
    protected boolean img;

    protected AbstractComponent(long id) {
        this.id = id;
    }

    protected AbstractComponent() {
    }

    public boolean isImg() {
        return img;
    }

    public void setImg(boolean img) {
        this.img = img;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        String imgPrefix = getClass().getSimpleName().toLowerCase() + "-";
        if (img) {
            return imgPrefix + id;
        }
        return imgPrefix + "default";
    }

}
