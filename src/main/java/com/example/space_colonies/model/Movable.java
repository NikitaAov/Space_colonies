package com.example.space_colonies.model;

/**
 * Объект, который может перемещаться по карте (лаб. 3).
 */
public interface Movable {

    boolean moveTo(Position newPosition);

    boolean canMoveTo(Position position);

    int getMovementRange();

    Position getCurrentPosition();
}
