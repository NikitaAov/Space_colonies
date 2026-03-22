package com.example.space_colonies.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Типобезопасная коллекция объектов на карте.
 */
public class SpaceObjectCollection<E extends SpaceObject> {

    private final List<E> entities;
    private final Map<String, E> byName;
    private final Set<Position> occupied;

    public SpaceObjectCollection() {
        this.entities = new ArrayList<>();
        this.byName = new HashMap<>();
        this.occupied = new HashSet<>();
    }

    public void add(E entity) {
        Objects.requireNonNull(entity, "entity");
        if (byName.containsKey(entity.getName())) {
            return;
        }
        entities.add(entity);
        byName.put(entity.getName(), entity);
        occupied.add(entity.getPosition());
    }

    public void remove(E entity) {
        if (entity == null) {
            return;
        }
        entities.remove(entity);
        byName.remove(entity.getName());
        occupied.remove(entity.getPosition());
    }

    public E getByName(String name) {
        return name == null ? null : byName.get(name);
    }

    public E getByPosition(Position position) {
        if (position == null) {
            return null;
        }
        return entities.stream()
                .filter(e -> e.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    public List<E> getActiveObjects() {
        return entities.stream()
                .filter(SpaceObject::isActive)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<E> findInRange(Position center, double range) {
        Objects.requireNonNull(center, "center");
        if (range < 0) {
            throw new IllegalArgumentException("range не может быть отрицательным: " + range);
        }
        return entities.stream()
                .filter(e -> e.getPosition().distanceTo(center) <= range + 1e-9)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void sortByName() {
        entities.sort(Comparator.comparing(SpaceObject::getName, String.CASE_INSENSITIVE_ORDER));
    }

    public void sortByCoordinates() {
        entities.sort(Comparator
                .comparingInt((SpaceObject e) -> e.getPosition().getX())
                .thenComparingInt(e -> e.getPosition().getY()));
    }

    public List<E> getAll() {
        return new ArrayList<>(entities);
    }

    public int size() {
        return entities.size();
    }

    public boolean isEmpty() {
        return entities.isEmpty();
    }

    public Set<Position> getOccupiedPositions() {
        return new HashSet<>(occupied);
    }

    public void clear() {
        entities.clear();
        byName.clear();
        occupied.clear();
    }
}
