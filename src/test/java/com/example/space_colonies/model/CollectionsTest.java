package com.example.space_colonies.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionsTest {

    @Test
    void inventoryStoresCargoAndMass() {
        Inventory<CargoItem> inv = new Inventory<>(5);
        assertTrue(inv.addItem("a", new CargoItem("X", 3)));
        assertFalse(inv.addItem("a", new CargoItem("Y", 1)));
        assertEquals(3, inv.totalCargoMass());
        assertEquals(1, inv.getUsedSlots());
    }

    @Test
    void spaceObjectCollectionFindsInRange() {
        SpaceObjectCollection<Planet> c = new SpaceObjectCollection<>();
        Planet p1 = new Planet("A", new Position(0, 0), 50, 40);
        Planet p2 = new Planet("B", new Position(2, 0), 60, 30);
        c.add(p1);
        c.add(p2);
        List<Planet> near = c.findInRange(new Position(0, 0), 2.5);
        assertEquals(2, near.size());
        c.sortByCoordinates();
        assertEquals("A", c.getAll().get(0).getName());
    }

    @Test
    void technologyManagerGroupsByCategory() {
        TechnologyManager<Technology> tm = new TechnologyManager<>();
        tm.addTechnology(new MiningAutomationTech());
        tm.addTechnology(new HyperdriveTech());
        Map<String, Integer> m = tm.summaryByCategory();
        assertTrue(m.size() >= 1);
        assertEquals(2, tm.getCount());
    }

    @Test
    void starSystemRegistryTracksSystemsAndColonies() {
        StarSystemRegistry reg = new StarSystemRegistry();
        Planet p = new Planet("Проксима-III", new Position(1, 1), 40, 50);
        reg.registerPlanet("Проксима", p);
        assertEquals(1, reg.getPlanetsInSystem("Проксима").size());
        reg.markColonizedPlanet("Проксима-III");
        List<String> with = reg.systemsWithKnownColonies();
        assertEquals(1, with.size());
        assertEquals("Проксима", with.get(0));
        Set<String> names = reg.getColonizedPlanetNames();
        assertTrue(names.contains("Проксима-III"));
    }
}
