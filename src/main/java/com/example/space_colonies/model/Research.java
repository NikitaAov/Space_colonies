package com.example.space_colonies.model;

/**
 * Исследовательская колония: дополнительная наука.
 */
public final class Research extends Colonizable {

    public Research(String name, Planet planet, int initialPopulation) {
        super(name, planet, initialPopulation);
    }

    @Override
    public String getColonyTypeLabel() {
        return "Исследовательская";
    }

    @Override
    public int getPopulationGrowthPerTurn() {
        int habit = planet.getHabitability();
        return 10 + habit / 20 + stationLevel * 5;
    }

    @Override
    public int getSciencePerTurnIfStation() {
        if (stationLevel < 1) {
            return 0;
        }
        return 8 + stationLevel * 2;
    }
}
