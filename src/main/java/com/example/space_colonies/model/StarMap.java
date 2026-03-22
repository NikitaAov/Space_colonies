package com.example.space_colonies.model;

/**
 * Сетка секторов с объектами
 */
public class StarMap {
    private final int width;
    private final int height;
    private final SpaceObject[][] cells;

    public StarMap(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Ширина и высота должны быть положительными: " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
        this.cells = new SpaceObject[width][height];
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

    public boolean placeSpaceObject(SpaceObject object) {
        if (object == null) {
            throw new IllegalArgumentException("object не может быть null");
        }
        Position pos = object.getPosition();
        if (!isValidPosition(pos)) {
            return false;
        }
        int x = pos.getX();
        int y = pos.getY();
        if (cells[x][y] != null) {
            return false;
        }
        cells[x][y] = object;
        return true;
    }

    /** Удобный метод для планет. */
    public boolean placePlanet(Planet planet) {
        return placeSpaceObject(planet);
    }

    public SpaceObject getSpaceObjectAt(Position position) {
        if (!isValidPosition(position)) {
            return null;
        }
        return cells[position.getX()][position.getY()];
    }

    public Planet getPlanetAt(Position position) {
        SpaceObject o = getSpaceObjectAt(position);
        return o instanceof Planet p ? p : null;
    }

    public void removeAt(Position position) {
        if (!isValidPosition(position)) {
            return;
        }
        cells[position.getX()][position.getY()] = null;
    }

    @Override
    public String toString() {
        return "StarMap " + width + "x" + height;
    }
}
