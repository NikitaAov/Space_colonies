package com.example.space_colonies.model;

/**
 * Орбитальная станция / форпост как объект карты (не планета).
 */
public final class SpaceStation extends SpaceObject {

    private int moduleLevel;
    private int maxModuleLevel;

    public SpaceStation(String name, Position position, int initialModuleLevel) {
        super(name, position);
        this.maxModuleLevel = 5;
        setModuleLevel(initialModuleLevel);
    }

    public int getModuleLevel() {
        return moduleLevel;
    }

    public void setModuleLevel(int moduleLevel) {
        if (moduleLevel < 0 || moduleLevel > maxModuleLevel) {
            throw new IllegalArgumentException("moduleLevel 0.." + maxModuleLevel);
        }
        this.moduleLevel = moduleLevel;
    }

    public int getMaxModuleLevel() {
        return maxModuleLevel;
    }

    @Override
    public void update() {
        if (isActive && moduleLevel < maxModuleLevel && moduleLevel > 0) {
            // лёгкое самообслуживание модулей
        }
    }

    @Override
    public String getDescription() {
        return name + " [модули " + moduleLevel + "/" + maxModuleLevel + "] " + position;
    }

    @Override
    public String scanReport() {
        return "Станция «" + name + "»: уровень модулей " + moduleLevel + ".";
    }
}
