package org.battleship.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlacementGenerator {

    private final Random random = new Random();

    public void placeAllShips(Board board) {

        List<ShipType> ships = new ArrayList<>();

        ships.add(ShipType.FOUR);
        ships.add(ShipType.THREE);
        ships.add(ShipType.THREE);
        ships.add(ShipType.TWO);
        ships.add(ShipType.TWO);
        ships.add(ShipType.TWO);
        ships.add(ShipType.ONE);
        ships.add(ShipType.ONE);
        ships.add(ShipType.ONE);
        ships.add(ShipType.ONE);

        Collections.shuffle(ships);

        for (ShipType type : ships) {
            placeSingleShip(board, type);
        }
    }

    private void placeSingleShip(Board board, ShipType type) {

        while (true) {
            boolean horizontal = random.nextBoolean();

            int startX = random.nextInt(Board.SIZE);
            int startY = random.nextInt(Board.SIZE);

            List<Point> positions = new ArrayList<>();

            boolean fits = true;

            for (int i = 0; i < type.size; i++) {
                int x = startX + (horizontal ? 0 : i);
                int y = startY + (horizontal ? i : 0);

                if (!board.isInside(x, y)) {
                    fits = false;
                    break;
                }

                positions.add(new Point(x, y));
            }

            if (!fits)
                continue;

            if (!isValidPlacement(board, positions))
                continue;

            board.addShip(new Ship(type, positions));
            break;
        }
    }

    private boolean isValidPlacement(Board board, List<Point> cells) {

        for (Point p : cells) {

            if (isBlocked(board, p.x, p.y))
                return false;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int xx = p.x + dx;
                    int yy = p.y + dy;

                    if (board.isInside(xx, yy) && isBlocked(board, xx, yy))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean isBlocked(Board board, int x, int y) {
        for (Ship ship : board.getShips()) {
            for (Point p : ship.getCells()) {
                if (p.x == x && p.y == y)
                    return true;
            }
        }
        return false;
    }
}