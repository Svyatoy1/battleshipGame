package org.battleship.ai;

import org.battleship.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerAI {

    private final Board opponentBoard;    // дошка гравця
    private final Random random = new Random();

    private AiMode mode = AiMode.HUNT;

    // Клітинки, в які AI вже влучив (для добивання)
    private final List<Point> hitCluster = new ArrayList<>();

    // Внутрішня пам'ять AI про стан клітин (те, що він "бачив")
    private final CellState[][] aiVision = new CellState[Board.SIZE][Board.SIZE];

    public ComputerAI(Board opponentBoard) {
        this.opponentBoard = opponentBoard;

        // AI спочатку "не знає" нічого про поле
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                aiVision[x][y] = CellState.UNKNOWN;
            }
        }
    }

    //    ГОЛОВНИЙ МЕТОД AI
    public Point chooseShot() {

        if (mode == AiMode.TARGET && !hitCluster.isEmpty()) {
            Point p = chooseTargetModeShot();
            if (p != null)
                return p;

            // якщо не знайшли ціль — повертаємось у HUNT
            mode = AiMode.HUNT;
        }

        return chooseHuntModeShot();
    }

    //   ОТРИМАННЯ РЕЗУЛЬТАТУ
    public void onShotResult(Point p, ShotResult result) {

        aiVision[p.x][p.y] = convertToVision(result);

        if (result == ShotResult.HIT) {
            hitCluster.add(p);
            mode = AiMode.TARGET;
        }
        else if (result == ShotResult.KILL) {
            hitCluster.add(p);
            markClusterAsSunk();
            hitCluster.clear();
            mode = AiMode.HUNT;
        }
    }

    private CellState convertToVision(ShotResult r) {
        return switch (r) {
            case MISS -> CellState.MISS;
            case HIT -> CellState.HIT;
            case KILL -> CellState.SUNK;
        };
    }

    //    TARGET MODE (каркас)
    private Point chooseTargetModeShot() {

        // На наступному кроці ми реалізуємо повну логіку добивання.
        // Поки — простий варіант: перевіряємо сусідів останнього попадання.

        Point base = hitCluster.get(hitCluster.size() - 1);

        int[][] dirs = {
                {1,0}, {-1,0}, {0,1}, {0,-1}
        };

        for (int[] d : dirs) {
            int nx = base.x + d[0];
            int ny = base.y + d[1];

            if (opponentBoard.isInside(nx, ny) && aiVision[nx][ny] == CellState.UNKNOWN) {
                return new Point(nx, ny);
            }
        }

        return null;
    }

    //    HUNT MODE (каркас)
    private Point chooseHuntModeShot() {

        // На наступному кроці тут буде алгоритм:
        //   - обчислення ймовірностей
        //   - пріоритет 4-палубника
        //
        // Поки ставимо просту логіку — випадковий постріл по UNKNOWN.

        while (true) {
            int x = random.nextInt(Board.SIZE);
            int y = random.nextInt(Board.SIZE);

            if (aiVision[x][y] == CellState.UNKNOWN) {
                return new Point(x, y);
            }
        }
    }

    //    ПОТОПЛЕНИЙ КЛАСТЕР
    private void markClusterAsSunk() {
        for (Point p : hitCluster) {
            aiVision[p.x][p.y] = CellState.SUNK;
        }
    }
}
