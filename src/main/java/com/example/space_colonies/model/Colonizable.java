package com.example.space_colonies.model;

/**
 * Базовый класс колоний: общие поля и поведение (типы освоения — Habitable, Mining, Research).
 */
public abstract class Colonizable implements Upgradeable {

    protected String name;
    protected Planet planet;
    protected int population;
    protected int stationLevel;
    protected int maxStationLevel;

    protected Colonizable(String name, Planet planet, int initialPopulation) {
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
        this.stationLevel = Math.min(stationLevel, maxStationLevel);
    }

    public abstract String getColonyTypeLabel();

    public abstract int getPopulationGrowthPerTurn();

    public int getStationMineralBonusPerTurn() {
        return stationLevel * 8;
    }

    /** Доп. наука за ход при stationLevel >= 1 (переопределяет Research). */
    public int getSciencePerTurnIfStation() {
        return 0;
    }

    public boolean upgradeStation() {
        if (stationLevel >= maxStationLevel) {
            return false;
        }
        stationLevel++;
        return true;
    }

    public int getNextStationBuildCost() {
        if (stationLevel >= maxStationLevel) {
            return -1;
        }
        return 150 + stationLevel * 100;
    }

    @Override
    public boolean upgrade() {
        return upgradeStation();
    }

    @Override
    public int getLevel() {
        return stationLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxStationLevel;
    }

    @Override
    public boolean canUpgrade() {
        return stationLevel < maxStationLevel;
    }

    @Override
    public int getUpgradeCost() {
        int c = getNextStationBuildCost();
        return Math.max(0, c);
    }

    @Override
    public String getLevelBonuses() {
        return "Станция ур. " + stationLevel + ": +" + (stationLevel * 8) + " к добыче минералов";
    }

    @Override
    public String toString() {
        return name + " на " + planet.getName() + " | население " + population
                + " | станция ур. " + stationLevel + "/" + maxStationLevel + " [" + getColonyTypeLabel() + "]";
    }
}
