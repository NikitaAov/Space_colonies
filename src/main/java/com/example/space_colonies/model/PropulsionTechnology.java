package com.example.space_colonies.model;

/**
 * Промежуточный класс: двигатели и манёвр (3-й уровень иерархии для гипердвигателя).
 */
public abstract class PropulsionTechnology extends Technology {

    private final int shipRangePerLevel;

    protected PropulsionTechnology(String name, int maxLevel, int scienceCostPerLevel, int shipRangePerLevel) {
        super(name, maxLevel, scienceCostPerLevel);
        if (shipRangePerLevel < 0) {
            throw new IllegalArgumentException("shipRangePerLevel не может быть отрицательным");
        }
        this.shipRangePerLevel = shipRangePerLevel;
    }

    @Override
    public final int mineralBonusPercentPerLevel() {
        return 0;
    }

    @Override
    public final int shipRangeBonusPerLevel() {
        return shipRangePerLevel;
    }

    @Override
    public String getTechCategory() {
        return "Двигатели";
    }
}
