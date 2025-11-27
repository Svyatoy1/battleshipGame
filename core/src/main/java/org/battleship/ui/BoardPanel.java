package org.battleship.ui;

import org.battleship.model.Board;
import org.battleship.model.CellState;
import org.battleship.model.Point;
import org.battleship.model.Ship;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    protected static final int CELL_SIZE = 40;
    protected static final int GRID_SIZE = 10;

    protected final Board board;

    public BoardPanel(Board board) {
        this.board = board;

        setPreferredSize(new Dimension(
                GRID_SIZE * CELL_SIZE,
                GRID_SIZE * CELL_SIZE
        ));
    }

    /**
     * Чи потрібно показувати кораблі цього поля?
     * За замовчуванням — так (ігрове поле гравця)
     */
    protected boolean shouldShowShips() {
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // фон
        g.setColor(new Color(0x084C61));
        g.fillRect(0, 0, getWidth(), getHeight());

        // сітка
        g.setColor(Color.WHITE);
        for (int i = 0; i <= GRID_SIZE; i++) {
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE);
            g.drawLine(0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE, i * CELL_SIZE);
        }

        // ---- МАЛЮЄМО КОРАБЛІ (лише якщо дозволено!) ----
        if (shouldShowShips()) {
            g.setColor(Color.DARK_GRAY);

            for (Ship ship : board.getShips()) {
                for (Point p : ship.getCells()) {
                    g.fillRect(
                            p.y * CELL_SIZE + 1,
                            p.x * CELL_SIZE + 1,
                            CELL_SIZE - 2,
                            CELL_SIZE - 2
                    );
                }
            }
        }

        // ---- МАЛЮЄМО СТАНИ КЛІТИНОК ----
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {

                CellState state = board.getCellState(row, col);

                switch (state) {
                    case HIT -> {
                        g.setColor(Color.RED);
                        g.fillOval(col * CELL_SIZE + 10, row * CELL_SIZE + 10, 20, 20);
                    }
                    case MISS -> {
                        g.setColor(Color.WHITE);
                        g.drawOval(col * CELL_SIZE + 10, row * CELL_SIZE + 10, 20, 20);
                    }
                    case SUNK -> {
                        g.setColor(Color.RED);
                        g.drawLine(col * CELL_SIZE + 5, row * CELL_SIZE + 5,
                                col * CELL_SIZE + CELL_SIZE - 5, row * CELL_SIZE + CELL_SIZE - 5);
                        g.drawLine(col * CELL_SIZE + CELL_SIZE - 5, row * CELL_SIZE + 5,
                                col * CELL_SIZE + 5, row * CELL_SIZE + CELL_SIZE - 5);
                    }
                }
            }
        }
    }
}