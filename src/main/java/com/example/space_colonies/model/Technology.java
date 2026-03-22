package com.example.space_colonies.model;

/**
 * Базовый класс исследуемых технологий
 */
public abstract class Technology {

    protected String name;
    protected int level;
    protected int maxLevel;
    protected int scienceCostPerLevel;

    protected Technology(String name, int maxLevel, int scienceCostPerLevel) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя технологии не может быть пустым");
        }
        if (maxLevel < 1) {
            throw new IllegalArgumentException("maxLevel должна быть >= 1: " + maxLevel);
        }
        if (scienceCostPerLevel < 0) {
            throw new IllegalArgumentException("scienceCostPerLevel не может быть отрицательным");
        }
        this.name = name;
        this.level = 0;
        this.maxLevel = maxLevel;
        this.scienceCostPerLevel = scienceCostPerLevel;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getScienceCostPerLevel() {
        return scienceCostPerLevel;
    }

    public abstract int mineralBonusPercentPerLevel();

    public abstract int shipRangeBonusPerLevel();

    public abstract String getTechCategory();

    public final int getNextLevelCost() {
        if (level >= maxLevel) {
            return -1;
        }
        return scienceCostPerLevel * (level + 1);
    }

    public final boolean researchLevel() {
        if (level >= maxLevel) {
            return false;
        }
        level++;
        return true;
    }

    public final int getTotalMineralBonusPercent() {
        return level * mineralBonusPercentPerLevel();
    }

    public final int getTotalShipRangeBonus() {
        return level * shipRangeBonusPerLevel();
    }

    @Override
    public String toString() {
        return name + " | ур. " + level + "/" + maxLevel + " | " + getTechCategory()
                + " | +" + getTotalMineralBonusPercent() + "% минералов, +" + getTotalShipRangeBonus() + " дальность";
    }
}
