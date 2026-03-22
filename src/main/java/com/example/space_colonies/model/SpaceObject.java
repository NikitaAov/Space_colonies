package com.example.space_colonies.model;

/**
 * Базовый класс объектов на звёздной карте (лаб. 2: космическая колонизация).
 */
public abstract class SpaceObject implements Scannable {
    protected String name;
    protected Position position;
    protected boolean isActive;

    public SpaceObject(String name, Position position) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (position == null) {
            throw new IllegalArgumentException("position не может быть null");
        }
        this.name = name;
        this.position = position;
        this.isActive = true;
    }

    public abstract void update();

    public abstract String getDescription();

    @Override
    public abstract String scanReport();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("position не может быть null");
        }
        this.position = position;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public double distanceTo(SpaceObject other) {
        if (other == null) {
            throw new IllegalArgumentException("other не может быть null");
        }
        return position.distanceTo(other.position);
    }

    @Override
    public String toString() {
        return name + " " + position + " [" + (isActive ? "активен" : "неактивен") + "]";
    }
}
