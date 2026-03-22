package com.example.space_colonies.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Индекс звёздных систем
 */
public class StarSystemRegistry {

    private final TreeMap<String, List<Planet>> systems;
    private final Set<String> colonizedPlanetNames;

    public StarSystemRegistry() {
        this.systems = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        this.colonizedPlanetNames = new HashSet<>();
    }

    /** Регистрация планеты в именованной звёздной системе. */
    public void registerPlanet(String systemName, Planet planet) {
        Objects.requireNonNull(planet, "planet");
        if (systemName == null || systemName.isBlank()) {
            throw new IllegalArgumentException("systemName не может быть пустым");
        }
        systems.computeIfAbsent(systemName, k -> new ArrayList<>()).add(planet);
    }

    public List<Planet> getPlanetsInSystem(String systemName) {
        if (systemName == null) {
            return List.of();
        }
        List<Planet> list = systems.get(systemName);
        return list == null ? List.of() : Collections.unmodifiableList(new ArrayList<>(list));
    }

    public Set<String> getSystemNames() {
        return Collections.unmodifiableSet(systems.keySet());
    }

    public void markColonizedPlanet(String planetName) {
        if (planetName != null && !planetName.isBlank()) {
            colonizedPlanetNames.add(planetName);
        }
    }

    public boolean isColonizedName(String planetName) {
        return planetName != null && colonizedPlanetNames.contains(planetName);
    }

    public Set<String> getColonizedPlanetNames() {
        return Collections.unmodifiableSet(new HashSet<>(colonizedPlanetNames));
    }

    /** Системы, в которых есть хотя бы одна колонизированная планета из реестра имён. */
    public List<String> systemsWithKnownColonies() {
        return systems.entrySet().stream()
                .filter(e -> e.getValue().stream()
                        .anyMatch(p -> colonizedPlanetNames.contains(p.getName())))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void clear() {
        systems.clear();
        colonizedPlanetNames.clear();
    }
}
