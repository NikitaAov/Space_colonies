package com.example.space_colonies.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Менеджер дерева технологий с упорядочиванием по имени
 */
public class TechnologyManager<T extends Technology> {

    private final TreeMap<String, T> byName;

    public TechnologyManager() {
        this.byName = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public void addTechnology(T technology) {
        Objects.requireNonNull(technology, "technology");
        byName.put(technology.getName(), technology);
    }

    public T getTechnology(String name) {
        return name == null ? null : byName.get(name);
    }

    public List<T> getTechnologiesBelowLevel(int maxLevelExclusive) {
        if (maxLevelExclusive < 0) {
            throw new IllegalArgumentException("maxLevelExclusive не может быть отрицательным");
        }
        return byName.values().stream()
                .filter(t -> t.getLevel() < maxLevelExclusive)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<T> getFullyResearched() {
        return byName.values().stream()
                .filter(t -> t.getLevel() >= t.getMaxLevel())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /** Сводка: категория → суммарный уровень исследований. */
    public Map<String, Integer> summaryByCategory() {
        return byName.values().stream()
                .collect(Collectors.groupingBy(
                        Technology::getTechCategory,
                        Collectors.summingInt(Technology::getLevel)));
    }

    public int totalResearchLevels() {
        return byName.values().stream().mapToInt(Technology::getLevel).sum();
    }

    public int getCount() {
        return byName.size();
    }

    public List<T> allTechnologies() {
        return new ArrayList<>(byName.values());
    }

    public void clear() {
        byName.clear();
    }
}
