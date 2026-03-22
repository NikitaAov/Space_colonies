package com.example.space_colonies.model;

/**
 * Участник боя (корабль, дрон и т.п.).
 */
public interface Combatable {

    int attack(Combatable target);

    void takeDamage(int damage);

    boolean canAttack(Combatable target);

    int getAttack();

    int getDefense();

    boolean isAlive();
}
