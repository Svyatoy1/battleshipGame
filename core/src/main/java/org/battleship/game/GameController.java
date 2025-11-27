package org.battleship.game;

import org.battleship.model.*;
import org.battleship.ai.*;

public class GameController {

    private final Board playerBoard;
    private final Board aiBoard;
    private final ComputerAI ai;

    private boolean playerTurn = true;

    public GameController() {
        PlacementGenerator gen = new PlacementGenerator();

        playerBoard = new Board();
        aiBoard = new Board();

        gen.placeAllShips(playerBoard);
        gen.placeAllShips(aiBoard);

        ai = new ComputerAI(playerBoard);
    }

    public ShotResult playerShoots(Point p) {
        if (!playerTurn) return null;

        ShotResult result = aiBoard.shoot(p);

        if (result == ShotResult.MISS) {
            playerTurn = false;
        }

        return result;
    }

    public ShotResult aiShootsOnce() {
        if (playerTurn) return null;

        Point target = ai.chooseShot();
        ShotResult result = playerBoard.shoot(target);

        ai.onShotResult(target, result);

        return result;
    }

    public void endAiTurn() {
        playerTurn = true;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public boolean isGameOver() {
        return playerBoard.allShipsSunk() || aiBoard.allShipsSunk();
    }

    public boolean didPlayerWin() {
        return aiBoard.allShipsSunk();
    }

    public ComputerAI getAi() {
        return ai;
    }

    public Board getPlayerBoard() { return playerBoard; }
    public Board getAiBoard() { return aiBoard; }
}