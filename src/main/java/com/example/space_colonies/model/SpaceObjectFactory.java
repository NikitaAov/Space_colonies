package com.example.space_colonies.model;

/**
 * Фабрика объектов карты.
 */
public final class SpaceObjectFactory {

    private SpaceObjectFactory() {
    }

    public static Planet createPlanet(String name, Position pos, int habitability, int minerals) {
        return new Planet(name, pos, habitability, minerals);
    }

    public static Asteroid createAsteroid(String name, Position pos, int mass, int richness) {
        return new Asteroid(name, pos, mass, richness);
    }

    public static SpaceStation createStation(String name, Position pos, int modules) {
        return new SpaceStation(name, pos, modules);
    }
}
