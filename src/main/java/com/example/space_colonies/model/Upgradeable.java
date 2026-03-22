package com.example.space_colonies.model;

/**
 * Объект с уровнями улучшения (станция колонии, технология).
 */
public interface Upgradeable {

    boolean upgrade();

    int getLevel();

    int getMaxLevel();

    boolean canUpgrade();

    int getUpgradeCost();

    String getLevelBonuses();
}
