package bunnybunny;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePlayPanel extends JPanel {

    private GameManager gameManager;
    private String playerName = "player";
    private int selectedBunny;
    private Image backgroundImage;
    private JLabel jlbplayName = new JLabel();
    private Image bunny1Image;
    private Image bunny2Image;
    int x = 550; // Initial x position of the bunny
    int y = 450; // Initial y position of the bunny
    int moveAmount = 10; // Number of pixels to move per key press
    private boolean positionChanged = false;

    public GamePlayPanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerName = gameManager.getPlayerName(); // Retrieve player name from GameManager
        setLayout(null);
        
        // Set focusable and request focus
        setFocusable(true);

        // Load images
        backgroundImage = new ImageIcon(getClass().getResource("BgGame.png")).getImage();
        bunny1Image = new ImageIcon(getClass().getResource("bunny1.png")).getImage();
        bunny2Image = new ImageIcon(getClass().getResource("bunny2.png")).getImage();

        // Display player name
        jlbplayName.setText(playerName);
        jlbplayName.setBounds(980, 3, 1000, 100);
        jlbplayName.setForeground(Color.BLACK);
        add(jlbplayName);

        // Add key listener for movement
        addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            positionChanged = false; // Reset flag on new key press
            if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
                x -= moveAmount;
                positionChanged = true; // Mark position as changed
            } else if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
                x += moveAmount;
                positionChanged = true; // Mark position as changed
            }
            x = Math.max(0, Math.min(x, getWidth() - 135));
            if (positionChanged) {
                System.out.println("Bunny moved to x: " + x + ", y: " + y);
            }
            repaint();
        }
    });

        // Add mouse listener for focus
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow(); // Request focus on mouse click
            }
        });

        // Add focus listener for debugging
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("GamePlayPanel has focus");
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("GamePlayPanel has lost focus");
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (positionChanged) {
//            System.out.println("Drawing bunny at x: " + x + ", y: " + y);
        }

        // Draw the selected bunny at the updated position
        if (selectedBunny == 1 && bunny1Image != null) {
            g.drawImage(bunny1Image, x, y, 135, 290, this);
        } else if (selectedBunny == 2 && bunny2Image != null) {
            g.drawImage(bunny2Image, x, y, 135, 290, this);
        }
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
}
