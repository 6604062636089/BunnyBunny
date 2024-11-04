package bunnybunny;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class GamePlayPanel extends JPanel {

    private GameManager gameManager;
    private String playerName = "player";
    private int selectedBunny;
    private Image backgroundImage;
    private JLabel jlbplayName = new JLabel();
    private Image bunny1Image;
    private Image bunny2Image;
    char ch = 'A';
    int x = 550; // Initial x position of the bunny
    int y = 450; // Initial y position of the bunny
    int moveAmount = 10; // Number of pixels to move per key press

    public GamePlayPanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerName = gameManager.getPlayerName(); // Retrieve player name from GameManager
        setLayout(null);

        // Load images
        backgroundImage = new ImageIcon(getClass().getResource("BgGame.png")).getImage();
        bunny1Image = new ImageIcon(getClass().getResource("bunny1.png")).getImage();
        bunny2Image = new ImageIcon(getClass().getResource("bunny2.png")).getImage();

        // Display player name
        jlbplayName.setText(playerName);
        jlbplayName.setBounds(980, 3, 1000, 100);
        jlbplayName.setForeground(Color.BLACK);
        add(jlbplayName);

        // Add KeyListener for movement
        addKeyListener(new KeyListener(){
                @Override
                public void keyTyped(KeyEvent e) {
                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    switch(e.getKeyCode()){
                        case KeyEvent.VK_W: y=y-10;break;  //KeyBord
                        case KeyEvent.VK_X: y=y+10;break;
                        case KeyEvent.VK_A: x=x-10; break;
                        case KeyEvent.VK_D: x=x+10;break;
                        default:
                            ch=e.getKeyChar();
                    }
                    repaint();
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                }
            });
        setFocusable(true); // Make sure the panel can gain focus for key events
    }

    // Setter for player name and selected bunny, updating UI when called
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        jlbplayName.setText(playerName);
    }

    public void setSelectedBunny(int selectedBunny) {
        this.selectedBunny = selectedBunny;
        repaint();
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw the selected bunny at the updated position
        if (selectedBunny == 1 && bunny1Image != null) {
            g.drawImage(bunny1Image, x, y, 135, 290, this);
        } else if (selectedBunny == 2 && bunny2Image != null) {
            g.drawImage(bunny2Image, x, y, 135, 290, this);
        }
    }
}
