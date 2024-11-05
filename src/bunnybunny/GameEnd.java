/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bunnybunny;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BrokenBarrierException;
import javax.swing.*;

/**
 *
 * @author This PC
 */
public class GameEnd extends JPanel {
    private GameManager gameManager;
    private int score;
    private String playerName;
    private JButton jbtplayAgain = new JButton("Play Again");
    private JLabel jlbscore = new JLabel();

    private Image backgroundImage;

    public GameEnd(GameManager gameManager) {
        setLayout(null);
        backgroundImage = new ImageIcon(getClass().getResource("BgEndGame.png")).getImage();
        
        this.gameManager = gameManager;
        
        jbtplayAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!playerName.isEmpty()) {
//                    gameManager.showSelectBunny(); // call Page SelectBunny
                      gameManager.playAgain();
                } else {
//                    JOptionPane.showMessageDialog(StartMenu.this, "Please enter your name.");
                }
            }
        });
        jbtplayAgain.setBounds(540, 520, 100, 50);
        add(jbtplayAgain);
        
        jlbscore.setFont(new Font("Arial", Font.BOLD, 50));
        jlbscore.setForeground(Color.BLACK);
        jlbscore.setBounds(580, 310, 100, 100); 
        
        add(jlbscore);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setScore(int countCarrot) {
        score = countCarrot;
        System.out.println("Your final scores : " + score);
        jlbscore.setText(String.valueOf(score));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
