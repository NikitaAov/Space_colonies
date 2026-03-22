package com.example.space_colonies.model;

/**
 * Исследуемая технология: уровни и бонус к эффективности колоний / корабля.
 */
public class Technology {
    private String name;
    private int level;
    private int maxLevel;
    /** Стоимость одного уровня в очках науки */
    private int scienceCostPerLevel;
    /** Бонус за уровень: +N% к добыче минералов на колониях (пример) */
    private int mineralBonusPercentPerLevel;
    /** +N к дальности корабля за уровень */
    private int shipRangeBonusPerLevel;

    public Technology(
            String name,
            int maxLevel,
            int scienceCostPerLevel,
            int mineralBonusPercentPerLevel,
            int shipRangeBonusPerLevel) {
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
        this.mineralBonusPercentPerLevel = mineralBonusPercentPerLevel;
        this.shipRangeBonusPerLevel = shipRangeBonusPerLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя технологии не может быть пустым");
        }
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level < 0 || level > maxLevel) {
            throw new IllegalArgumentException("level должна быть в диапазоне 0.." + maxLevel + ": " + level);
        }
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        if (maxLevel < 1) {
            throw new IllegalArgumentException("maxLevel должна быть >= 1");
        }
        this.maxLevel = maxLevel;
        this.level = Math.min(level, maxLevel);
    }

    public int getScienceCostPerLevel() {
        return scienceCostPerLevel;
    }

    public void setScienceCostPerLevel(int scienceCostPerLevel) {
        if (scienceCostPerLevel < 0) {
            throw new IllegalArgumentException("scienceCostPerLevel не может быть отрицательным");
        }
        this.scienceCostPerLevel = scienceCostPerLevel;
    }

    public int getMineralBonusPercentPerLevel() {
        return mineralBonusPercentPerLevel;
    }

    public void setMineralBonusPercentPerLevel(int mineralBonusPercentPerLevel) {
        if (mineralBonusPercentPerLevel < 0) {
            throw new IllegalArgumentException("mineralBonusPercentPerLevel не может быть отрицательным");
        }
        this.mineralBonusPercentPerLevel = mineralBonusPercentPerLevel;
    }

    public int getShipRangeBonusPerLevel() {
        return shipRangeBonusPerLevel;
    }

    public void setShipRangeBonusPerLevel(int shipRangeBonusPerLevel) {
        if (shipRangeBonusPerLevel < 0) {
            throw new IllegalArgumentException("shipRangeBonusPerLevel не может быть отрицательным");
        }
        this.shipRangeBonusPerLevel = shipRangeBonusPerLevel;
    }

    /** Стоимость следующего уровня в науке. */
    public int getNextLevelCost() {
        if (level >= maxLevel) {
            return -1;
        }
        return scienceCostPerLevel * (level + 1);
    }

    /** Попытка исследовать уровень (ресурсы списывает игра). */
    public boolean researchLevel() {
        if (level >= maxLevel) {
            return false;
        }
        level++;
        return true;
    }

    public int getTotalMineralBonusPercent() {
        return level * mineralBonusPercentPerLevel;
    }

    public int getTotalShipRangeBonus() {
        return level * shipRangeBonusPerLevel;
    }

    @Override
    public String toString() {
        return name + " | ур. " + level + "/" + maxLevel
                + " | +" + getTotalMineralBonusPercent() + "% минералов, +" + getTotalShipRangeBonus() + " дальность";
    }
}
