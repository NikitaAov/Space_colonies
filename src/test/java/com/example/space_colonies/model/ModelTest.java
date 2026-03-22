package com.example.space_colonies.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelTest {

    @Test
    void positionDistanceAndValidation() {
        Position a = new Position(0, 0);
        Position b = new Position(3, 4);
        assertEquals(5.0, a.distanceTo(b), 1e-9);
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 0));
    }

    @Test
    void resourceConsumeAndAdd() {
        Resource r = new Resource("Тест", 10, 100);
        assertTrue(r.consumeAmount(5));
        assertEquals(5, r.getAmount());
        assertTrue(r.addAmount(90));
        assertEquals(95, r.getAmount());
        assertFalse(r.addAmount(10));
    }

    @Test
    void planetColonization() {
        Position p = new Position(1, 1);
        Planet planet = new Planet("P1", p, 50, 40);
        assertFalse(planet.isColonized());
        Colony c = new Colony("C1", planet, 100);
        assertTrue(planet.establishColony(c));
        assertTrue(planet.isColonized());
        assertThrows(IllegalArgumentException.class, () -> new Planet("", p, 50, 50));
    }

    @Test
    void spaceshipMove() {
        Spaceship s = new Spaceship("S", new Position(0, 0), 500, 10, 3, 50);
        assertTrue(s.moveTo(new Position(2, 0)));
        assertEquals(2, s.getPosition().getX());
    }

    @Test
    void technologyResearch() {
        Technology t = new Technology("T", 2, 10, 3, 0);
        assertEquals(10, t.getNextLevelCost());
        assertTrue(t.researchLevel());
        assertEquals(1, t.getLevel());
    }

    @Test
    void starMapPlace() {
        StarMap map = new StarMap(5, 5);
        Planet pl = new Planet("X", new Position(2, 2), 60, 60);
        assertTrue(map.placePlanet(pl));
        assertEquals(pl, map.getPlanetAt(new Position(2, 2)));
        assertFalse(map.placePlanet(new Planet("Y", new Position(2, 2), 50, 50)));
    }
}
