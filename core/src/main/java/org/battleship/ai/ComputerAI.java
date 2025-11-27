package org.battleship.ai;

import org.battleship.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerAI {
    private Point lastShot;
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

        // TARGET MODE
        if (mode == AiMode.TARGET && !hitCluster.isEmpty()) {
            Point p = chooseTargetModeShot();
            if (p != null) {
                lastShot = p;
                return p;
            }

            // якщо ціль не знайдено → повертаємось у HUNT
            mode = AiMode.HUNT;
        }

        // HUNT MODE
        Point shot = chooseHuntModeShot();
        lastShot = shot;
        return shot;
    }

    public Point getLastShot() {
        return lastShot;
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

    private Point shootAroundSingleHit(Point base) {

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

    private Point detectDirection() {

        if (hitCluster.size() < 2)
            return null;

        Point a = hitCluster.get(0);
        for (Point b : hitCluster) {

            // горизонтально
            if (a.x == b.x && a.y != b.y) {
                return new Point(0, 1); // горизонтальний
            }

            // вертикально
            if (a.y == b.y && a.x != b.x) {
                return new Point(1, 0); // вертикальний
            }
        }

        return null; // ще не можемо визначити напрям
    }

    private Point shootAlongDirection(Point dir) {

        // Сортуємо попадання вздовж напрямку — від меншої координати до більшої
        hitCluster.sort((p1, p2) ->
                dir.x != 0 ? Integer.compare(p1.x, p2.x)
                        : Integer.compare(p1.y, p2.y)
        );

        // Пробуємо продовжити "вперед"
        Point last = hitCluster.get(hitCluster.size() - 1);

        int fx = last.x + dir.x;
        int fy = last.y + dir.y;

        if (opponentBoard.isInside(fx, fy) && aiVision[fx][fy] == CellState.UNKNOWN) {
            return new Point(fx, fy);
        }

        // Пробуємо продовжити "назад"
        Point first = hitCluster.get(0);

        int bx = first.x - dir.x;
        int by = first.y - dir.y;

        if (opponentBoard.isInside(bx, by) && aiVision[bx][by] == CellState.UNKNOWN) {
            return new Point(bx, by);
        }

        return null;
    }

    //    TARGET MODE (каркас)
    private Point chooseTargetModeShot() {

        // 1) Якщо одне попадання — шукаємо сусідів
        if (hitCluster.size() == 1) {
            return shootAroundSingleHit(hitCluster.get(0));
        }

        // 2) Якщо два або більше попадань — визначаємо напрям
        Point dir = detectDirection();

        if (dir != null) {
            return shootAlongDirection(dir);
        }

        // 3) fallback: якщо щось пішло не так — шукаємо сусідів усіх попадань
        for (Point p : hitCluster) {
            Point around = shootAroundSingleHit(p);
            if (around != null)
                return around;
        }

        return null; // переключимося в hunt mode
    }

    private Point chooseRandomUnknownCell() {
        while (true) {
            int x = random.nextInt(Board.SIZE);
            int y = random.nextInt(Board.SIZE);
            if (aiVision[x][y] == CellState.UNKNOWN)
                return new Point(x, y);
        }
    }
    
    //    HUNT MODE (каркас)
    private Point chooseHuntModeShot() {

        int n = Board.SIZE;
        int shipLen = 4;  // пріоритет — шукаємо 4-палубний

        int[][] score = new int[n][n];

        // Горизонтальні варіанти
        for (int x = 0; x < n; x++) {
            for (int y = 0; y <= n - shipLen; y++) {

                boolean valid = true;

                for (int k = 0; k < shipLen; k++) {
                    int yy = y + k;

                    if (aiVision[x][yy] == CellState.MISS ||
                            aiVision[x][yy] == CellState.SUNK) {
                        valid = false;
                        break;
                    }
                }

                if (!valid) continue;

                for (int k = 0; k < shipLen; k++) {
                    int yy = y + k;
                    if (aiVision[x][yy] == CellState.UNKNOWN ||
                            aiVision[x][yy] == CellState.HIT) {
                        score[x][yy]++;
                    }
                }
            }
        }

        //  Вертикальні варіанти
        for (int x = 0; x <= n - shipLen; x++) {
            for (int y = 0; y < n; y++) {

                boolean valid = true;

                for (int k = 0; k < shipLen; k++) {
                    int xx = x + k;

                    if (aiVision[xx][y] == CellState.MISS ||
                            aiVision[xx][y] == CellState.SUNK) {
                        valid = false;
                        break;
                    }
                }

                if (!valid) continue;

                for (int k = 0; k < shipLen; k++) {
                    int xx = x + k;
                    if (aiVision[xx][y] == CellState.UNKNOWN ||
                            aiVision[xx][y] == CellState.HIT) {
                        score[xx][y]++;
                    }
                }
            }
        }

        //  Вибираємо клітинку з максимальним score
        int bestScore = -1;
        Point best = null;

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {

                if (aiVision[x][y] != CellState.UNKNOWN)
                    continue;

                if (score[x][y] > bestScore) {
                    bestScore = score[x][y];
                    best = new Point(x, y);
                }
            }
        }

        // fallback — якщо всі score нульові
        if (best == null) {
            return chooseRandomUnknownCell();
        }

        return best;
    }

    //    ПОТОПЛЕНИЙ КЛАСТЕР
    private void markClusterAsSunk() {
        for (Point p : hitCluster) {
            aiVision[p.x][p.y] = CellState.SUNK;
        }
    }
}
