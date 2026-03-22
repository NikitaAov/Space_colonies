package com.example.space_colonies.model;

/**
 * Колония с упором на рост населения и экологию.
 */
public final class Habitable extends Colonizable {

    public Habitable(String name, Planet planet, int initialPopulation) {
        super(name, planet, initialPopulation);
    }

    @Override
    public String getColonyTypeLabel() {
        return "Обитаемая";
    }

    @Override
    public int getPopulationGrowthPerTurn() {
        int habit = planet.getHabitability();
        return (int) ((10 + habit / 20.0 + stationLevel * 5) * 1.15);
    }
}
