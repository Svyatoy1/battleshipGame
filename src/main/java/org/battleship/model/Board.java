package org.battleship.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int SIZE = 10;

    private final CellState[][] states = new CellState[SIZE][SIZE];
    private final List<Ship> ships = new ArrayList<>();

    public Board() {
        // всі клітинки стартують як UNKNOWN
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                states[x][y] = CellState.UNKNOWN;
            }
        }
    }

    public CellState getCellState(int x, int y) {
        return states[x][y];
    }

    public void setCellState(int x, int y, CellState state) {
        states[x][y] = state;
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }
}