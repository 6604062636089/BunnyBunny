/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bunnybunny;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 *
 * @author This PC
 */
public class GameStartPanel extends JPanel {

    private Image backgroundImage;
    private Image startIcon;  
    private Rectangle startIconBounds;  

    public GameStartPanel(GameManager gameManager) {
        setLayout(null);
        
        backgroundImage = new ImageIcon(getClass().getResource("BgGameStart.png")).getImage();
        startIcon = new ImageIcon(getClass().getResource("StartIcon.png")).getImage();
        
        startIconBounds = new Rectangle(200, 150, 600, 350);  
        
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY(), gameManager);  
            }
        });
    }
    
    private void handleClick(int mouseX, int mouseY, GameManager gameManager) {
        if (startIconBounds.contains(mouseX, mouseY)) {
            gameManager.startGame();  //start game when click startIcon
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(backgroundImage != null){
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        //startIcon
        if (startIcon != null) {
            g.drawImage(startIcon, startIconBounds.x, startIconBounds.y, startIconBounds.width, startIconBounds.height, this);
        }
    }
}
