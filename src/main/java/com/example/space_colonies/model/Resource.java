package com.example.space_colonies.model;

/**
 * Игровой ресурс с ограничением по максимуму.
 */
public class Resource {
    private String name;
    private int amount;
    private int maxAmount;

    public Resource(String name, int initialAmount, int maxAmount) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя ресурса не может быть пустым");
        }
        if (maxAmount < 0) {
            throw new IllegalArgumentException("maxAmount не может быть отрицательным: " + maxAmount);
        }
        this.name = name;
        this.maxAmount = maxAmount;
        if (initialAmount < 0) {
            throw new IllegalArgumentException("Начальное количество не может быть отрицательным: " + initialAmount);
        }
        this.amount = Math.min(initialAmount, maxAmount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя ресурса не может быть пустым");
        }
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        if (maxAmount < 0) {
            throw new IllegalArgumentException("maxAmount не может быть отрицательным");
        }
        this.maxAmount = maxAmount;
        if (amount > maxAmount) {
            this.amount = maxAmount;
        }
    }

    public void setAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Количество не может быть отрицательным: " + amount);
        }
        if (amount > maxAmount) {
            throw new IllegalArgumentException("Количество превышает maxAmount: " + amount + " > " + maxAmount);
        }
        this.amount = amount;
    }

    public boolean addAmount(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value должно быть неотрицательным: " + value);
        }
        if (amount + value <= maxAmount) {
            amount += value;
            return true;
        }
        return false;
    }

    public boolean consumeAmount(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value должно быть неотрицательным: " + value);
        }
        if (amount >= value) {
            amount -= value;
            return true;
        }
        return false;
    }

    public boolean isFull() {
        return amount >= maxAmount;
    }

    public boolean isEmpty() {
        return amount <= 0;
    }

    @Override
    public String toString() {
        return name + ": " + amount + "/" + maxAmount;
    }
}
