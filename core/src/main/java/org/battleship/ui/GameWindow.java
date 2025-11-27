package org.battleship.ui;

import org.battleship.game.GameController;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        GameController controller = new GameController();

        PlayerBoardPanel playerPanel = new PlayerBoardPanel(controller.getPlayerBoard());
        EnemyBoardPanel enemyPanel = new EnemyBoardPanel(controller, playerPanel);

        setLayout(new GridLayout(1, 2));

        add(playerPanel);
        add(enemyPanel);

        setVisible(true);
    }
}