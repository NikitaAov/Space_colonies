package com.example.space_colonies.model;

/**
 * Источник продукции за ход (планета, станция).
 */
public interface Producible {

    int produce();

    int getProductionRate();

    boolean canProduce();

    int getProductionCost();

    String getProductType();
}
