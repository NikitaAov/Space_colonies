package com.example.space_colonies.model;

/**
 * Добывающая колония: усиленная добыча, меньше прирост населения.
 */
public final class Mining extends Colonizable {

    public Mining(String name, Planet planet, int initialPopulation) {
        super(name, planet, initialPopulation);
    }

    @Override
    public String getColonyTypeLabel() {
        return "Добывающая";
    }

    @Override
    public int getPopulationGrowthPerTurn() {
        int habit = planet.getHabitability();
        return (int) ((10 + habit / 20.0 + stationLevel * 5) * 0.85);
    }

    @Override
    public int getStationMineralBonusPerTurn() {
        return stationLevel * 12;
    }
}
