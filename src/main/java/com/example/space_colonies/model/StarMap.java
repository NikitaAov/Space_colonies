package com.example.space_colonies.model;

/**
 * Сетка секторов с планетами
 */
public class StarMap {
    private final int width;
    private final int height;
    private final Planet[][] planets;

    public StarMap(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Ширина и высота должны быть положительными: " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
        this.planets = new Planet[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isValidPosition(Position position) {
        if (position == null) {
            return false;
        }
        int x = position.getX();
        int y = position.getY();
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean placePlanet(Planet planet) {
        if (planet == null) {
            throw new IllegalArgumentException("planet не может быть null");
        }
        Position pos = planet.getPosition();
        if (!isValidPosition(pos)) {
            return false;
        }
        int x = pos.getX();
        int y = pos.getY();
        if (planets[x][y] != null) {
            return false;
        }
        planets[x][y] = planet;
        return true;
    }

    public Planet getPlanetAt(Position position) {
        if (!isValidPosition(position)) {
            return null;
        }
        return planets[position.getX()][position.getY()];
    }

    public void removePlanetAt(Position position) {
        if (!isValidPosition(position)) {
            return;
        }
        planets[position.getX()][position.getY()] = null;
    }

    @Override
    public String toString() {
        return "StarMap " + width + "x" + height;
    }
}
