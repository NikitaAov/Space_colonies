package com.example.space_colonies.model;

/**
 * Планета на карте: пригодность, минералы, колония (если освоена).
 */
public class Planet {
    private String name;
    private Position position;
    /** Пригодность для жизни, 0–100 */
    private int habitability;
    /** Богатство недр, 0–100 */
    private int mineralRichness;
    private Colony colony;

    public Planet(String name, Position position, int habitability, int mineralRichness) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя планеты не может быть пустым");
        }
        if (position == null) {
            throw new IllegalArgumentException("position не может быть null");
        }
        setHabitability(habitability);
        setMineralRichness(mineralRichness);
        this.name = name;
        this.position = position;
        this.colony = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя планеты не может быть пустым");
        }
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("position не может быть null");
        }
        this.position = position;
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

    public Colony getColony() {
        return colony;
    }

    public boolean isColonized() {
        return colony != null;
    }

    /**
     * Осваивает планету: создаёт колонию. Планета должна быть свободна.
     */
    public boolean establishColony(Colony newColony) {
        if (newColony == null) {
            throw new IllegalArgumentException("newColony не может быть null");
        }
        if (colony != null) {
            return false;
        }
        colony = newColony;
        return true;
    }

    /** Базовая стоимость колонизации (энергия), чем ниже пригодность — тем дороже. */
    public int getColonizationEnergyCost() {
        return 200 + (100 - habitability) * 2;
    }

    /** Минералы с планеты за ход (до модификаторов станций). */
    public int getBaseMineralYieldPerTurn() {
        return 5 + mineralRichness / 10;
    }

    @Override
    public String toString() {
        String status = isColonized() ? "колония: " + colony.getName() : "не освоена";
        return name + " " + position + " | пригодность " + habitability + "%, руда " + mineralRichness + "% | " + status;
    }
}
