package com.example.space_colonies.model;

/**
 * Промежуточный класс: технологии инфраструктуры и добычи (3-й уровень иерархии).
 */
public abstract class InfrastructureTechnology extends Technology {

    private final int mineralBonusPerLevel;

    protected InfrastructureTechnology(String name, int maxLevel, int scienceCostPerLevel, int mineralBonusPerLevel) {
        super(name, maxLevel, scienceCostPerLevel);
        if (mineralBonusPerLevel < 0) {
            throw new IllegalArgumentException("mineralBonusPerLevel не может быть отрицательным");
        }
        this.mineralBonusPerLevel = mineralBonusPerLevel;
    }

    @Override
    public final int mineralBonusPercentPerLevel() {
        return mineralBonusPerLevel;
    }

    @Override
    public final int shipRangeBonusPerLevel() {
        return 0;
    }

    @Override
    public String getTechCategory() {
        return "Инфраструктура";
    }
}
