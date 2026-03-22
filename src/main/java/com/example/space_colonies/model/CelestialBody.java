package com.example.space_colonies.model;

/**
 * Небесное тело (шаблонный метод: общий ход для планет и астероидов).
 */
public abstract class CelestialBody extends SpaceObject {

    protected int massClass;

    public CelestialBody(String name, Position position, int massClass) {
        super(name, position);
        setMassClass(massClass);
    }

    public int getMassClass() {
        return massClass;
    }

    public void setMassClass(int massClass) {
        if (massClass < 1 || massClass > 10) {
            throw new IllegalArgumentException("massClass должна быть в диапазоне 1–10: " + massClass);
        }
        this.massClass = massClass;
    }

    @Override
    public final void update() {
        if (!isActive) {
            return;
        }
        onCelestialTurn();
    }

    /** Шаг симуляции для конкретного тела (полиморфизм). */
    protected abstract void onCelestialTurn();
}
