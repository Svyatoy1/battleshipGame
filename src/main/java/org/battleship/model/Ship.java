package org.battleship.model;

import java.util.List;

public class Ship {

    private final ShipType type;
    private final List<Point> cells;
    private int hits;          // кількість влучань
    private boolean sunk;

    public Ship(ShipType type, List<Point> cells) {
        this.type = type;
        this.cells = cells;
        this.hits = 0;
        this.sunk = false;
    }

    public ShipType getType() {
        return type;
    }

    public List<Point> getCells() {
        return cells;
    }

    public boolean isSunk() {
        return sunk;
    }

    public void registerHit() {
        hits++;
        if (hits >= type.size) {
            sunk = true;
        }
    }
}
