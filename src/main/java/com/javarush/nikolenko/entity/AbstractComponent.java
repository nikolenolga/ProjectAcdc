package com.javarush.nikolenko.entity;

public abstract class AbstractComponent {
    protected long id;

    public AbstractComponent(long id) {
        this.id = id;
    }

    public AbstractComponent() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
