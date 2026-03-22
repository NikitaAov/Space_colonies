package com.example.space_colonies.model;

/**
 * Планета: небесное тело с атмосферой и возможностью колонизации.
 */
public class Planet extends CelestialBody implements Producible {

    private int habitability;
    private int mineralRichness;
    private Colonizable colony;

    public Planet(String name, Position position, int habitability, int mineralRichness) {
        this(name, position, habitability, mineralRichness, 5);
    }

    public Planet(String name, Position position, int habitability, int mineralRichness, int massClass) {
        super(name, position, massClass);
        setHabitability(habitability);
        setMineralRichness(mineralRichness);
        this.colony = null;
    }

    public int getHabitability() {
        return habitability;
    }

    public void setHabitability(int habitability) {
        if (habitability < 0 || habitability > 100) {
            throw new IllegalArgumentException("habitability должна быть в диапазоне 0–100: " + habitability);
        }
        this.habitability = habitability;
    }

    public int getMineralRichness() {
        return mineralRichness;
    }

    public void setMineralRichness(int mineralRichness) {
        if (mineralRichness < 0 || mineralRichness > 100) {
            throw new IllegalArgumentException("mineralRichness должна быть в диапазоне 0–100: " + mineralRichness);
        }
        this.mineralRichness = mineralRichness;
    }

    public Colonizable getColony() {
        return colony;
    }

    public boolean isColonized() {
        return colony != null;
    }

    public boolean establishColony(Colonizable newColony) {
        if (newColony == null) {
            throw new IllegalArgumentException("newColony не может быть null");
        }
        if (colony != null) {
            return false;
        }
        colony = newColony;
        return true;
    }

    public int getColonizationEnergyCost() {
        return 200 + (100 - habitability) * 2;
    }

    public int getBaseMineralYieldPerTurn() {
        return 5 + mineralRichness / 10;
    }

    @Override
    public int produce() {
        return getProductionRate();
    }

    @Override
    public int getProductionRate() {
        return getBaseMineralYieldPerTurn();
    }

    @Override
    public boolean canProduce() {
        return isActive();
    }

    @Override
    public int getProductionCost() {
        return 0;
    }

    @Override
    public String getProductType() {
        return "Минералы";
    }

    @Override
    protected void onCelestialTurn() {
        // климат/орбита — без побочных эффектов в базовой лабе
    }

    @Override
    public String getDescription() {
        String status = isColonized() ? "колония: " + colony.getName() : "не освоена";
        return name + " " + position + " | пригодность " + habitability + "%, руда " + mineralRichness + "% | " + status;
    }

    @Override
    public String scanReport() {
        return "Планета «" + name + "»: пригодность " + habitability + "%, минералы " + mineralRichness + "%.";
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
