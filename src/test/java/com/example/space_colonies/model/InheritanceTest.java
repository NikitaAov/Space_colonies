package com.example.space_colonies.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InheritanceTest {

    @Test
    void polymorphismSpaceObjects() {
        List<SpaceObject> objects = new ArrayList<>();
        objects.add(new Planet("P", new Position(0, 0), 50, 50));
        objects.add(new Asteroid("A", new Position(1, 0), 2, 40));
        objects.add(new SpaceStation("S", new Position(2, 0), 1));
        for (SpaceObject o : objects) {
            assertNotNull(o.getDescription());
            assertNotNull(o.scanReport());
            o.update();
        }
    }

    @Test
    void polymorphismColonizable() {
        Planet p1 = new Planet("Pl1", new Position(0, 0), 60, 50);
        Planet p2 = new Planet("Pl2", new Position(1, 0), 50, 60);
        Planet p3 = new Planet("Pl3", new Position(2, 0), 55, 55);
        List<Colonizable> list = List.of(
                new Habitable("H", p1, 100),
                new Mining("M", p2, 100),
                new Research("R", p3, 100));
        for (Colonizable c : list) {
            assertTrue(c.getPopulationGrowthPerTurn() > 0);
            assertNotNull(c.getColonyTypeLabel());
        }
    }

    @Test
    void polymorphismTechnology() {
        List<Technology> techs = List.of(new MiningAutomationTech(), new HyperdriveTech());
        for (Technology t : techs) {
            assertNotNull(t.getTechCategory());
            assertTrue(t.getNextLevelCost() > 0);
        }
    }
}
