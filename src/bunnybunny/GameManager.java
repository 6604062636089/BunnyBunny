/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bunnybunny;

import javax.swing.*;
import java.awt.CardLayout;

/**
 *
 * @author This PC
 */
public class GameManager extends JPanel {

    private CardLayout cardLayout;
    private StartMenu startMenu;
    private SelectBunny selectBunny;
    private GameStartPanel gameStartPanel;
    private GamePlayPanel gamePlayPanel;
    private Thread gameThread;
    
    private String playerName;
    private int selectedBunny;
    private int countCarrot;

    public GameManager() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        //create Page
        startMenu = new StartMenu(this);
        selectBunny = new SelectBunny(this);
        gameStartPanel = new GameStartPanel(this);
        gamePlayPanel = new GamePlayPanel(this);

        add(startMenu, "StartMenu");
        add(selectBunny, "SelectBunny");
        add(gameStartPanel, "GameStart");
        add(gamePlayPanel, "GamePlay");

        showStartMenu();
    }

    public int getSelectedBunny() {
        return selectedBunny;
    }

    public void setSelectedBunny(int selectedBunny) {
        this.selectedBunny = selectedBunny;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setCountCarrot(int countCarrot) {
        this.countCarrot = countCarrot;
    }

    public int getCountCarrot() {
        return countCarrot;
    }

    public void showStartMenu() {
        cardLayout.show(this, "StartMenu");
    }

    public void showSelectBunny() {
        cardLayout.show(this, "SelectBunny");
    }

    public void showGameStart() {
        cardLayout.show(this, "GameStart");
        gamePlayPanel.requestFocusInWindow();
    }

    public void startGame() {
        gamePlayPanel.setPlayerName(playerName);
        gamePlayPanel.setSelectedBunny(selectedBunny);
        cardLayout.show(this, "GamePlay");
        System.out.println("Game Started! with player name : " + playerName);

        //start Thread game
        if (gameThread == null || !gameThread.isAlive()) { //not created || stopped work
            gameThread = new Thread(new GameLoop());
            gameThread.start();
        }
    }
    
    //inner class loop game
    private class GameLoop implements Runnable {

        @Override
        public void run() {
            while (true) {
                updateGame();

                repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void updateGame() {

    }
}
