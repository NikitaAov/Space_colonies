package com.example.space_colonies.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Универсальный трюм/склад с ограничением по числу слотов
 */
public class Inventory<T> {

    private final Map<String, T> items;
    private final int maxSlots;
    private int usedSlots;

    public Inventory(int maxSlots) {
        if (maxSlots < 0) {
            throw new IllegalArgumentException("maxSlots не может быть отрицательным: " + maxSlots);
        }
        this.items = new HashMap<>();
        this.maxSlots = maxSlots;
        this.usedSlots = 0;
    }

    public boolean addItem(String slotId, T item) {
        Objects.requireNonNull(item, "item");
        if (slotId == null || slotId.isBlank()) {
            throw new IllegalArgumentException("slotId не может быть пустым");
        }
        if (items.containsKey(slotId)) {
            return false;
        }
        if (usedSlots >= maxSlots) {
            return false;
        }
        items.put(slotId, item);
        usedSlots++;
        return true;
    }

    public T getItem(String slotId) {
        return items.get(slotId);
    }

    public T removeItem(String slotId) {
        T removed = items.remove(slotId);
        if (removed != null) {
            usedSlots--;
        }
        return removed;
    }

    public boolean containsSlot(String slotId) {
        return items.containsKey(slotId);
    }

    public List<T> getAllItems() {
        return new ArrayList<>(items.values());
    }

    public Set<String> getSlotIds() {
        return new HashSet<>(items.keySet());
    }

    /** Суммарная «масса» для CargoItem, иначе 0. */
    public int totalCargoMass() {
        return items.values().stream()
                .filter(CargoItem.class::isInstance)
                .mapToInt(o -> ((CargoItem) o).getMassUnits())
                .sum();
    }

    public List<String> describeSlots() {
        return items.entrySet().stream()
                .map(e -> e.getKey() + " → " + e.getValue())
                .collect(Collectors.toList());
    }

    public int getUsedSlots() {
        return usedSlots;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public boolean isFull() {
        return usedSlots >= maxSlots;
    }

    public boolean isEmpty() {
        return usedSlots == 0;
    }

    public void clear() {
        items.clear();
        usedSlots = 0;
    }
}
