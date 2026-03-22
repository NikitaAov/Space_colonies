package com.example.space_colonies.model;

/**
 * Космический корабль: перемещение, топливо, груз для колонизации (лаб. 3: {@link Movable}, {@link Combatable}).
 */
public class Spaceship implements Movable, Combatable {
    private String name;
    private Position position;
    private int fuel;
    private int maxFuel;
    private int cargo;
    private int cargoCapacity;
    private int movementRange;
    private int hull;
    private int maxHull;

    public Spaceship(
            String name,
            Position position,
            int maxFuel,
            int cargoCapacity,
            int movementRange,
            int maxHull) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя корабля не может быть пустым");
        }
        if (position == null) {
            throw new IllegalArgumentException("position не может быть null");
        }
        if (maxFuel <= 0) {
            throw new IllegalArgumentException("maxFuel должно быть положительным: " + maxFuel);
        }
        if (cargoCapacity < 0) {
            throw new IllegalArgumentException("cargoCapacity не может быть отрицательным");
        }
        if (movementRange <= 0) {
            throw new IllegalArgumentException("movementRange должно быть положительным: " + movementRange);
        }
        if (maxHull <= 0) {
            throw new IllegalArgumentException("maxHull должно быть положительным: " + maxHull);
        }
        this.name = name;
        this.position = position;
        this.maxFuel = maxFuel;
        this.fuel = maxFuel;
        this.cargoCapacity = cargoCapacity;
        this.cargo = 0;
        this.movementRange = movementRange;
        this.maxHull = maxHull;
        this.hull = maxHull;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя корабля не может быть пустым");
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

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        if (fuel < 0 || fuel > maxFuel) {
            throw new IllegalArgumentException("fuel должна быть в диапазоне 0.." + maxFuel);
        }
        this.fuel = fuel;
    }

    public int getMaxFuel() {
        return maxFuel;
    }

    public void setMaxFuel(int maxFuel) {
        if (maxFuel <= 0) {
            throw new IllegalArgumentException("maxFuel должно быть положительным");
        }
        this.maxFuel = maxFuel;
        this.fuel = Math.min(fuel, maxFuel);
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        if (cargo < 0 || cargo > cargoCapacity) {
            throw new IllegalArgumentException("cargo должна быть в диапазоне 0.." + cargoCapacity);
        }
        this.cargo = cargo;
    }

    public int getCargoCapacity() {
        return cargoCapacity;
    }

    public void setCargoCapacity(int cargoCapacity) {
        if (cargoCapacity < 0) {
            throw new IllegalArgumentException("cargoCapacity не может быть отрицательным");
        }
        this.cargoCapacity = cargoCapacity;
        this.cargo = Math.min(cargo, cargoCapacity);
    }

    public int getMovementRange() {
        return movementRange;
    }

    public void setMovementRange(int movementRange) {
        if (movementRange <= 0) {
            throw new IllegalArgumentException("movementRange должно быть положительным");
        }
        this.movementRange = movementRange;
    }

    public int getHull() {
        return hull;
    }

    public void setHull(int hull) {
        if (hull < 0 || hull > maxHull) {
            throw new IllegalArgumentException("hull должна быть в диапазоне 0.." + maxHull);
        }
        this.hull = hull;
    }

    public int getMaxHull() {
        return maxHull;
    }

    public void setMaxHull(int maxHull) {
        if (maxHull <= 0) {
            throw new IllegalArgumentException("maxHull должно быть положительным");
        }
        this.maxHull = maxHull;
        this.hull = Math.min(hull, maxHull);
    }

    /** Расход топлива ~ 10 единиц на 1 единицу расстояния (округление вверх). */
    public static int fuelCostForDistance(double distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("distance не может быть отрицательной");
        }
        return (int) Math.ceil(distance * 10);
    }

    /**
     * Перемещение на новую клетку карты. Требует достаточно топлива и дистанции в пределах range.
     */
    public boolean moveTo(Position newPosition) {
        return moveTo(newPosition, movementRange);
    }

    /**
     * Перемещение с заданной максимальной дистанцией за ход (например, с учётом технологий).
     */
    public boolean moveTo(Position newPosition, int effectiveMaxRange) {
        if (newPosition == null) {
            throw new IllegalArgumentException("newPosition не может быть null");
        }
        if (effectiveMaxRange <= 0) {
            throw new IllegalArgumentException("effectiveMaxRange должно быть положительным: " + effectiveMaxRange);
        }
        double dist = position.distanceTo(newPosition);
        if (dist > effectiveMaxRange + 1e-9) {
            return false;
        }
        int cost = fuelCostForDistance(dist);
        if (fuel < cost) {
            return false;
        }
        fuel -= cost;
        position = newPosition;
        return true;
    }

    public boolean loadCargo(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount не может быть отрицательным");
        }
        if (cargo + amount <= cargoCapacity) {
            cargo += amount;
            return true;
        }
        return false;
    }

    public boolean unloadCargo(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount не может быть отрицательным");
        }
        if (cargo >= amount) {
            cargo -= amount;
            return true;
        }
        return false;
    }

    public void repair(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount не может быть отрицательным");
        }
        hull = Math.min(maxHull, hull + amount);
    }

    public boolean isOperational() {
        return hull > 0;
    }

    public void refuel(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount не может быть отрицательным");
        }
        fuel = Math.min(maxFuel, fuel + amount);
    }

    @Override
    public boolean canMoveTo(Position position) {
        if (position == null) {
            return false;
        }
        return position.distanceTo(this.position) <= movementRange + 1e-9;
    }

    @Override
    public Position getCurrentPosition() {
        return position;
    }

    @Override
    public int getAttack() {
        return Math.max(5, maxHull / 8);
    }

    @Override
    public int getDefense() {
        return Math.max(2, maxHull / 12);
    }

    @Override
    public boolean isAlive() {
        return hull > 0;
    }

    @Override
    public boolean canAttack(Combatable target) {
        if (!isAlive() || target == null || !target.isAlive()) {
            return false;
        }
        if (target instanceof Spaceship other) {
            return position.distanceTo(other.position) <= 1.5 + 1e-9;
        }
        return false;
    }

    @Override
    public int attack(Combatable target) {
        if (!canAttack(target)) {
            return 0;
        }
        int dmg = getAttack() + (int) (Math.random() * 4);
        target.takeDamage(dmg);
        return dmg;
    }

    @Override
    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("damage не может быть отрицательным");
        }
        int actual = Math.max(1, damage - getDefense());
        hull = Math.max(0, hull - actual);
    }

    @Override
    public String toString() {
        return name + " " + position + " | топливо " + fuel + "/" + maxFuel
                + " | груз " + cargo + "/" + cargoCapacity + " | корпус " + hull + "/" + maxHull;
    }
}
