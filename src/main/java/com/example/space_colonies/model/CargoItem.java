package com.example.space_colonies.model;

import java.io.Serializable;

/**
 * Единица груза/оборудования для трюма.
 */
public final class CargoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    /** Условная масса в единицах грузоподъёмности. */
    private final int massUnits;

    public CargoItem(String name, int massUnits) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя груза не может быть пустым");
        }
        if (massUnits < 0) {
            throw new IllegalArgumentException("massUnits не может быть отрицательным: " + massUnits);
        }
        this.name = name;
        this.massUnits = massUnits;
    }

    public String getName() {
        return name;
    }

    public int getMassUnits() {
        return massUnits;
    }

    @Override
    public String toString() {
        return name + " (" + massUnits + " ед.)";
    }
}
