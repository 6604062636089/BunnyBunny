/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bunnybunny;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 *
 * @author This PC
 */
public class SelectBunny extends JPanel {
    private Image backgroundImage;
    private Image bunny1Image;
    private Image bunny2Image;
    
    private Rectangle bunny1Bounds;
    private Rectangle bunny2Bounds;
    private GameManager gameManager;

    public SelectBunny(GameManager gameManager) {
        
        this.gameManager = gameManager;
        
        backgroundImage = new ImageIcon(getClass().getResource("BgSelectBunny.png")).getImage();
        bunny1Image = new ImageIcon(getClass().getResource("bunny1.png")).getImage();
        bunny2Image = new ImageIcon(getClass().getResource("bunny2.png")).getImage();
        
        bunny1Bounds = new Rectangle(200, 150, 300, 600); 
        bunny2Bounds = new Rectangle(680, 175, 300, 550);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
        
        setLayout(null);
    }
    
    private void handleClick(int mouseX, int mouseY) {
        
        if (bunny1Bounds.contains(mouseX, mouseY)) {
            System.out.println("Select bunny1");
            gameManager.setSelectedBunny(1);
            gameManager.startGame();  
        }
        
        if (bunny2Bounds.contains(mouseX, mouseY)) {
            System.out.println("Select bunny2");
            gameManager.setSelectedBunny(2);
            gameManager.startGame(); 
        }
    }

   @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        g.drawImage(bunny1Image, bunny1Bounds.x, bunny1Bounds.y, bunny1Bounds.width, bunny1Bounds.height, this);
        g.drawImage(bunny2Image, bunny2Bounds.x, bunny2Bounds.y, bunny2Bounds.width, bunny2Bounds.height, this);
    }
    
}