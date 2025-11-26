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

    private void markShipAsSunk(Ship ship) {

        for (Point p : ship.getCells()) {
            states[p.x][p.y] = CellState.SUNK;
        }
    }

    public ShotResult shoot(int x, int y) {

        // Якщо по цій клітинці вже стріляли — нічого не робимо
        if (states[x][y] == CellState.MISS || states[x][y] == CellState.HIT || states[x][y] == CellState.SUNK) {
            return ShotResult.MISS; // нейтральний варіант
        }

        // Шукаємо корабель у цій клітинці
        Ship ship = findShipAt(x, y);

        if (ship == null) {
            // Промах
            states[x][y] = CellState.MISS;
            return ShotResult.MISS;
        }

        // Влучання
        ship.registerHit();
        states[x][y] = CellState.HIT;

        if (ship.isSunk()) {
            markShipAsSunk(ship);
            return ShotResult.KILL;
        }

        return ShotResult.HIT;
    }

    public ShotResult shoot(Point p) {
        return shoot(p.x, p.y);
    }

    private Ship findShipAt(int x, int y) {
        for (Ship ship : ships) {
            for (Point p : ship.getCells()) {
                if (p.x == x && p.y == y)
                    return ship;
            }
        }
        return null;
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    public CellState getCellState(int x, int y) {
        return states[x][y];
    }

    public void setCellState(int x, int y, CellState state) {
        states[x][y] = state;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }
}