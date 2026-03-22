package com.example.space_colonies.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Игровой ресурс с ограничением по максимуму. Реализует Tradeable и Comparable.
 */
public class Resource implements Tradeable, Comparable<Resource>, Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private int amount;
    private int maxAmount;
    /** Условная цена единицы для торговли */
    private int tradeValuePerUnit;

    public Resource(String name, int initialAmount, int maxAmount) {
        this(name, initialAmount, maxAmount, 1);
    }

    public Resource(String name, int initialAmount, int maxAmount, int tradeValuePerUnit) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя ресурса не может быть пустым");
        }
        if (maxAmount < 0) {
            throw new IllegalArgumentException("maxAmount не может быть отрицательным: " + maxAmount);
        }
        if (tradeValuePerUnit < 0) {
            throw new IllegalArgumentException("tradeValuePerUnit не может быть отрицательным");
        }
        this.name = name;
        this.maxAmount = maxAmount;
        this.tradeValuePerUnit = tradeValuePerUnit;
        if (initialAmount < 0) {
            throw new IllegalArgumentException("Начальное количество не может быть отрицательным: " + initialAmount);
        }
        this.amount = Math.min(initialAmount, maxAmount);
    }

    public int getTradeValuePerUnit() {
        return tradeValuePerUnit;
    }

    public void setTradeValuePerUnit(int tradeValuePerUnit) {
        if (tradeValuePerUnit < 0) {
            throw new IllegalArgumentException("tradeValuePerUnit не может быть отрицательным");
        }
        this.tradeValuePerUnit = tradeValuePerUnit;
    }

    @Override
    public int getValue() {
        return amount * tradeValuePerUnit;
    }

    @Override
    public boolean canBeSold() {
        return amount > 0;
    }

    @Override
    public boolean canBeBought() {
        return amount < maxAmount;
    }

    @Override
    public String getTradeType() {
        if (canBeSold() && canBeBought()) {
            return "Продажа/Покупка";
        }
        if (canBeSold()) {
            return "Только продажа";
        }
        if (canBeBought()) {
            return "Только покупка";
        }
        return "Не торгуется";
    }

    @Override
    public List<String> getTradeRequirements() {
        List<String> list = new ArrayList<>();
        if (amount > 0) {
            list.add("Наличие ресурса");
        }
        list.add("Разрешение на торговлю");
        return list;
    }

    @Override
    public int compareTo(Resource o) {
        return Integer.compare(o.getValue(), getValue());
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
        return name + ": " + amount + "/" + maxAmount + " | ценность: " + getValue();
    }
}
