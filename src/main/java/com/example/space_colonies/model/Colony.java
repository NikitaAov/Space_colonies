package com.example.space_colonies.model;

/**
 * Колония на планете: население и уровень орбитальной станции (модули).
 */
public class Colony {
    private String name;
    private Planet planet;
    private int population;
    private int stationLevel;
    private int maxStationLevel;

    public Colony(String name, Planet planet, int initialPopulation) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя колонии не может быть пустым");
        }
        if (planet == null) {
            throw new IllegalArgumentException("planet не может быть null");
        }
        this.name = name;
        this.planet = planet;
        this.maxStationLevel = 3;
        setPopulation(initialPopulation);
        this.stationLevel = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя колонии не может быть пустым");
        }
        this.name = name;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        if (planet == null) {
            throw new IllegalArgumentException("planet не может быть null");
        }
        this.planet = planet;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        if (population < 0) {
            throw new IllegalArgumentException("population не может быть отрицательным: " + population);
        }
        this.population = population;
    }

    public int getStationLevel() {
        return stationLevel;
    }

    public void setStationLevel(int stationLevel) {
        if (stationLevel < 0 || stationLevel > maxStationLevel) {
            throw new IllegalArgumentException("stationLevel должна быть в диапазоне 0.." + maxStationLevel);
        }
        this.stationLevel = stationLevel;
    }

    public int getMaxStationLevel() {
        return maxStationLevel;
    }

    public void setMaxStationLevel(int maxStationLevel) {
        if (maxStationLevel < 1) {
            throw new IllegalArgumentException("maxStationLevel должна быть >= 1: " + maxStationLevel);
        }
        this.maxStationLevel = maxStationLevel;
    }

    /** Прирост населения за ход (зависит от пригодности планеты). */
    public int getPopulationGrowthPerTurn() {
        int habit = planet.getHabitability();
        return 10 + habit / 20 + stationLevel * 5;
    }

    /** Дополнительная добыча минералов за уровень станции. */
    public int getStationMineralBonusPerTurn() {
        return stationLevel * 8;
    }

    /** Следующий уровень орбитальной станции (стоимость ресурсов проверяется в игре). */
    public boolean upgradeStation() {
        if (stationLevel >= maxStationLevel) {
            return false;
        }
        stationLevel++;
        return true;
    }

    /** Стоимость следующего уровня станции в материалах. */
    public int getNextStationBuildCost() {
        if (stationLevel >= maxStationLevel) {
            return -1;
        }
        return 150 + stationLevel * 100;
    }

    @Override
    public String toString() {
        return name + " на " + planet.getName() + " | население " + population
                + " | станция ур. " + stationLevel + "/" + maxStationLevel;
    }
}
