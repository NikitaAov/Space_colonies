package com.example.space_colonies.model;

/**
 * Астероидное поле: богатая руда, нет атмосферы.
 */
public final class Asteroid extends CelestialBody {

    private int iceAndMetalRichness;

    public Asteroid(String name, Position position, int massClass, int iceAndMetalRichness) {
        super(name, position, massClass);
        setIceAndMetalRichness(iceAndMetalRichness);
    }

    public int getIceAndMetalRichness() {
        return iceAndMetalRichness;
    }

    public void setIceAndMetalRichness(int iceAndMetalRichness) {
        if (iceAndMetalRichness < 0 || iceAndMetalRichness > 100) {
            throw new IllegalArgumentException("iceAndMetalRichness 0–100");
        }
        this.iceAndMetalRichness = iceAndMetalRichness;
    }

    public int getQuickMiningYield() {
        return 3 + iceAndMetalRichness / 15;
    }

    @Override
    protected void onCelestialTurn() {
    }

    @Override
    public String getDescription() {
        return name + " [астероид] масса " + massClass + " богатство " + iceAndMetalRichness + "% " + position;
    }

    @Override
    public String scanReport() {
        return "Астероид «" + name + "»: пригодность для быстрой добычи " + iceAndMetalRichness + "%.";
    }
}
