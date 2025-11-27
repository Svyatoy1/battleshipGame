package org.battleship.game;

import org.battleship.model.*;
import org.battleship.ai.*;

public class GameController {

    private final Board playerBoard;   // поле гравця
    private final Board aiBoard;       // поле ШІ
    private final ComputerAI ai;       // сам комп'ютер

    private boolean playerTurn = true; // хто ходить зараз?

    public GameController() {
        PlacementGenerator gen = new PlacementGenerator();

        // Створюємо дошки
        playerBoard = new Board();
        aiBoard = new Board();

        // Генеруємо розміщення кораблів
        gen.placeAllShips(playerBoard);
        gen.placeAllShips(aiBoard);

        // Створюємо штучний інтелект
        ai = new ComputerAI(playerBoard);
    }

    //     ГРАВЕЦЬ СТРІЛЯЄ
    public ShotResult playerShoots(Point p) {

        if (!playerTurn)
            return null; // не його хід

        ShotResult result = aiBoard.shoot(p);

        // Якщо промах — хід переходить до комп'ютера
        if (result == ShotResult.MISS) {
            playerTurn = false;
        }

        return result;
    }

    //     AI СТРІЛЯЄ
    public ShotResult aiShoots() {

        if (playerTurn)
            return null; // не його хід

        // AI обирає клітинку
        Point target = ai.chooseShot();

        // AI стріляє
        ShotResult result = playerBoard.shoot(target);

        // AI повинен знати результат, щоб оновити стратегію
        ai.onShotResult(target, result);

        // Якщо промах — хід переходить гравцю
        if (result == ShotResult.MISS) {
            playerTurn = true;
        }

        return result;
    }

    //     ХТО ЗАРАЗ ХОДИТЬ?
    public boolean isPlayerTurn() {
        return playerTurn;
    }

    //       КІНЕЦЬ ГРИ?
    public boolean isGameOver() {
        return playerBoard.allShipsSunk() || aiBoard.allShipsSunk();
    }

    public boolean didPlayerWin() {
        return aiBoard.allShipsSunk();
    }

    //      ГЕТЕРИ ДЛЯ UI/ТЕСТУ
    public Board getPlayerBoard() {
        return playerBoard;
    }

    public Board getAiBoard() {
        return aiBoard;
    }
}
