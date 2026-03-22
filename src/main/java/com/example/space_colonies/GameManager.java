package com.example.space_colonies;

import com.example.space_colonies.model.Combatable;
import com.example.space_colonies.model.Movable;
import com.example.space_colonies.model.Position;
import com.example.space_colonies.model.Producible;
import com.example.space_colonies.model.Technology;
import com.example.space_colonies.model.Tradeable;
import com.example.space_colonies.model.Upgradeable;

import java.util.ArrayList;
import java.util.List;

/**
 * Полиморфная работа с сущностями по интерфейсам (лаб. 3).
 */
public class GameManager {

    private final List<Movable> movableEntities = new ArrayList<>();
    private final List<Combatable> combatEntities = new ArrayList<>();
    private final List<Producible> productionEntities = new ArrayList<>();
    private final List<Upgradeable> upgradeableEntities = new ArrayList<>();
    private final List<Tradeable> tradeableEntities = new ArrayList<>();

    public void addMovable(Movable movable) {
        if (movable != null) {
            movableEntities.add(movable);
        }
    }

    public void addCombatable(Combatable combatable) {
        if (combatable != null) {
            combatEntities.add(combatable);
        }
    }

    public void addProducible(Producible producible) {
        if (producible != null) {
            productionEntities.add(producible);
        }
    }

    /** Только технологии: колонии улучшаются через игру с расходом материалов. */
    public void addUpgradeableTechnology(Upgradeable technology) {
        if (technology != null) {
            upgradeableEntities.add(technology);
        }
    }

    public void addTradeable(Tradeable tradeable) {
        if (tradeable != null) {
            tradeableEntities.add(tradeable);
        }
    }

    public void moveAllToward(Position targetPosition) {
        for (Movable movable : movableEntities) {
            if (movable.canMoveTo(targetPosition)) {
                movable.moveTo(targetPosition);
            }
        }
    }

    public int processAllProduction() {
        int total = 0;
        for (Producible p : productionEntities) {
            if (p.canProduce()) {
                total += p.produce();
            }
        }
        return total;
    }

    /** Исследование без траты науки (демонстрация интерфейса); в реальной игре уровни даёт {@link com.example.space_colonies.Game#tryResearch}. */
    public void researchAllTechnologiesOneStep() {
        for (Upgradeable u : upgradeableEntities) {
            if (u instanceof Technology && u.canUpgrade()) {
                u.upgrade();
            }
        }
    }

    public int getTotalTradeValue() {
        return tradeableEntities.stream().mapToInt(Tradeable::getValue).sum();
    }

    public void displayCombatSnapshot() {
        System.out.println("Боевые объекты (интерфейс Combatable):");
        for (Combatable c : combatEntities) {
            String st = c.isAlive() ? "боеготов" : "выведен";
            System.out.println("  " + st + " | атака " + c.getAttack() + ", защита " + c.getDefense());
        }
    }

    public List<Movable> getMovableEntities() {
        return List.copyOf(movableEntities);
    }

    public List<Combatable> getCombatEntities() {
        return List.copyOf(combatEntities);
    }
}
