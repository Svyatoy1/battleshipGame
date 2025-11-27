package org.battleship.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {

    private static final int CELL_SIZE = 40;
    private static final int GRID_SIZE = 10;

    public BoardPanel() {
        setPreferredSize(new Dimension(
                GRID_SIZE * CELL_SIZE,
                GRID_SIZE * CELL_SIZE
        ));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;

                System.out.println("Clicked: row=" + row + " col=" + col);

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Фон
        g.setColor(new Color(0x084C61));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Лінії
        g.setColor(Color.WHITE);
        for (int i = 0; i <= GRID_SIZE; i++) {
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE);
            g.drawLine(0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE, i * CELL_SIZE);
        }

        // Тут будемо малювати кораблі та попадання
    }
}