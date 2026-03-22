package com.example.space_colonies.model;

import java.util.List;

/**
 * Ресурс или товар, доступный для обмена.
 */
public interface Tradeable {

    int getValue();

    boolean canBeSold();

    boolean canBeBought();

    String getTradeType();

    List<String> getTradeRequirements();
}
