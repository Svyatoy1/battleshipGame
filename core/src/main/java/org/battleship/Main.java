package org.battleship;

import org.battleship.ui.GameWindow;

public class Main {
    public static void main(String[] args) {
        new GameWindow();
    }
}

//import org.battleship.model.*;
//import org.battleship.game.GameController;

// java.util.Scanner;

/*public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=== МОРСЬКИЙ БІЙ (КОНСОЛЬНА ВЕРСІЯ) ===\n");

        GameController game = new GameController();

        while (!game.isGameOver()) {

            printBoards(game);

            if (game.isPlayerTurn()) {
                playerTurn(game);
            } else {
                aiTurn(game);
            }
        }

        System.out.println("\n=== ГРА ЗАВЕРШЕНА ===");

        if (game.didPlayerWin()) {
            System.out.println("🎉 ВИ ПЕРЕМОГЛИ!");
        } else {
            System.out.println("💀 AI ПЕРЕМІГ...");
        }
    }

    // ============================
    //         ХІД ГРАВЦЯ
    // ============================
    private static void playerTurn(GameController game) {
        System.out.println("\nВаш хід!");

        while (true) {
            System.out.print("Введіть координати (формат X Y): ");

            int x = scanner.nextInt();
            int y = scanner.nextInt();

            if (!validCoord(x, y)) {
                System.out.println("❌ Некоректні координати! Введіть від 0 до 9.");
                continue;
            }

            ShotResult result = game.playerShoots(new Point(x, y));

            if (result == null) {
                System.out.println("❌ Зараз не ваш хід!");
                continue;
            }

            switch (result) {
                case MISS -> {
                    System.out.println("💧 Промах!");
                    return; // Хід переходить до AI
                }
                case HIT -> System.out.println("🔥 Влучили!");
                case KILL -> System.out.println("💥 Потопили корабель!");
            }

            if (result == ShotResult.HIT || result == ShotResult.KILL) {
                System.out.println("🎯 Ви ходите ще раз!");
            }

            if (game.isGameOver()) return;
        }
    }

    // ============================
    //           ХІД AI
    // ============================
    private static void aiTurn(GameController game) {
        System.out.println("\nХід AI...");

        while (!game.isPlayerTurn() && !game.isGameOver()) {
            ShotResult result = game.aiShoots();

            switch (result) {
                case MISS -> System.out.println("AI: 💧 Промах!");
                case HIT -> System.out.println("AI: 🔥 Влучання!");
                case KILL -> System.out.println("AI: 💥 Потопили ваш корабель!");
            }

            if (result == ShotResult.MISS) return;

            if (result == ShotResult.HIT || result == ShotResult.KILL) {
                System.out.println("AI ходить ще раз!");
            }
        }
    }

    // ============================
    //         ВІДМАЛЬОВКА
    // ============================
    private static void printBoards(GameController game) {

        Board player = game.getPlayerBoard();
        Board ai = game.getAiBoard();

        System.out.println("\nВаше поле:");
        printBoard(player, true);

        System.out.println("\nПоле AI (туман війни):");
        printBoard(ai, false);
    }


    private static void printBoard(Board board, boolean revealShips) {

        System.out.print("   ");
        for (int y = 0; y < Board.SIZE; y++) System.out.print(y + " ");
        System.out.println();

        for (int x = 0; x < Board.SIZE; x++) {

            final int fx = x;
            System.out.print(x + ": ");

            for (int y = 0; y < Board.SIZE; y++) {

                final int fy = y;

                CellState state = board.getCellState(fx, fy);

                boolean hasShip = board.getShips()
                        .stream()
                        .anyMatch(ship -> ship.getCells().stream().anyMatch(p -> p.x == fx && p.y == fy));

                if (state == CellState.HIT) System.out.print("X ");
                else if (state == CellState.SUNK) System.out.print("# ");
                else if (state == CellState.MISS) System.out.print("· ");
                else {
                    if (revealShips && hasShip) System.out.print("O ");
                    else System.out.print("~ ");
                }
            }
            System.out.println();
        }
    }

    private static boolean validCoord(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }
}*/