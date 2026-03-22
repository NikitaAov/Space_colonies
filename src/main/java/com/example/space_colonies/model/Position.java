package com.example.space_colonies.model;

import java.util.Objects;

/**
 * Координата на звёздной карте.
 */
public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("x не может быть отрицательным: " + x);
        }
        this.x = x;
    }

    public void setY(int y) {
        if (y < 0) {
            throw new IllegalArgumentException("y не может быть отрицательным: " + y);
        }
        this.y = y;
    }

    public double distanceTo(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("other не может быть null");
        }
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
