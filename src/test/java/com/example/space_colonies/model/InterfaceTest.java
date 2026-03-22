package com.example.space_colonies.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InterfaceTest {

    @Test
    void spaceshipIsMovableAndCombatable() {
        Spaceship s = new Spaceship("X", new Position(0, 0), 100, 10, 3, 50);
        assertInstanceOf(Movable.class, s);
        assertInstanceOf(Combatable.class, s);
        assertTrue(s.canMoveTo(new Position(2, 0)));
        assertEquals(s.getPosition().getX(), s.getCurrentPosition().getX());
    }

    @Test
    void planetIsProducible() {
        Planet p = new Planet("P", new Position(1, 1), 50, 40);
        assertInstanceOf(Producible.class, p);
        assertTrue(p.getProductionRate() > 0);
        assertTrue(p.canProduce());
    }

    @Test
    void resourceIsTradeableAndComparable() {
        Resource r1 = new Resource("A", 10, 100, 2);
        Resource r2 = new Resource("B", 5, 50, 5);
        assertInstanceOf(Tradeable.class, r1);
        assertTrue(r1.getValue() > 0);
        assertTrue(r1.compareTo(r2) > 0);
    }

    @Test
    void colonyIsUpgradeable() {
        Planet p = new Planet("Pl", new Position(0, 0), 60, 50);
        Colonizable c = new Habitable("H", p, 100);
        assertInstanceOf(Upgradeable.class, c);
        assertTrue(c.canUpgrade());
    }

    @Test
    void technologyIsUpgradeable() {
        Technology t = new MiningAutomationTech();
        assertInstanceOf(Upgradeable.class, t);
        assertTrue(t.canUpgrade());
    }
}
