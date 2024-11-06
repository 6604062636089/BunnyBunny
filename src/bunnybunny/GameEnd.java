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
        
        System.out.println("final score");
        
        this.gameManager = gameManager;
        
        jbtplayAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!playerName.isEmpty()) {
                      gameManager.playAgain();
                } else {
                    //JOptionPane.showMessageDialog(StartMenu.this, "Please enter your name.");
                }
            }
        });
        jbtplayAgain.setBounds(540, 520, 100, 50);
        add(jbtplayAgain);
        
        jlbscore.setFont(new Font("Arial", Font.BOLD, 50));
        jlbscore.setForeground(Color.BLACK);
        
        
        add(jlbscore);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setScore(int countCarrot) {
        score = countCarrot;
        if(score < 10) {
            jlbscore.setBounds(580, 315, 100, 100);
        } else {
            jlbscore.setBounds(560, 315, 100, 100); 
        }
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
