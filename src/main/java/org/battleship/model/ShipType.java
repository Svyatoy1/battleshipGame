package org.battleship.model;

public enum ShipType {
    FOUR(4),
    THREE(3),
    TWO(2),
    ONE(1);

    public final int size;

    ShipType(int size) {
        this.size = size;
    }
}