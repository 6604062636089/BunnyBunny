package bunnybunny;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePlayPanel extends JPanel implements Runnable {

    private GameManager gameManager;
    private String playerName = "player";
    private int selectedBunny;
    private Image backgroundImage;
    private JLabel jlbplayName = new JLabel();
    private Image bunny1Image;
    private Image bunny2Image;
    private Image carrotImage;
    private int countCarrot = 0;
    int x = 550; // Initial x position of the bunny
    int y = 450; // Initial y position of the bunny
    int moveAmount = 10; // Number of pixels to move per key press
    private boolean positionChanged = false;
    private JLabel jlbcountCorrot = new JLabel();

    // Carrot properties
    private int carrotX;
    private int carrotY;
    private boolean carrotVisible = false;
    private Random random = new Random();

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
        carrotImage = new ImageIcon(getClass().getResource("carrot.png")).getImage(); // Load carrot image

        // Display player name
        jlbplayName.setText(playerName);
        jlbplayName.setBounds(980, 3, 1000, 100);
        jlbplayName.setForeground(Color.BLACK);
        add(jlbplayName);

        // Generate initial carrot position
        generateCarrot();

        // Start thread for falling carrot
        Thread carrotThread = new Thread(this);
//        carrotThread.start();

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
                checkCarrotCollection(); // Check for carrot collection
                repaint();
            }
        });

        // Add mouse listener for focus
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow(); // Request focus on mouse click
                
                // Start thread for falling carrot
//        Thread carrotThread = new Thread(this);
        carrotThread.start();
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

    // Generate carrot at a random position
    private void generateCarrot() {
        int panelWidth = getWidth();
        if (panelWidth <= 50) {
            // Handle case when panel width is too small
            carrotX = 0; // Position carrot at the left edge
        } else {
            carrotX = random.nextInt(panelWidth - 50); // Random x position for carrot
        }
        carrotY = 0; // Start from the top of the panel
        carrotVisible = true; // Set carrot to visible
    }

    // Check if the bunny has collected the carrot
    private void checkCarrotCollection() {
        Rectangle bunnyRect = new Rectangle(x, y, 80, 180);
        Rectangle carrotRect = new Rectangle(carrotX, carrotY, 25, 25); // Assuming carrot is 50x50
        if (bunnyRect.intersects(carrotRect)) {
            countCarrot += 1;
            System.out.println("Carrot collected!");
            System.out.println("Your scores : " + countCarrot);
            carrotVisible = false; // Hide carrot after collection
            setCarrotCountText(countCarrot);
            generateCarrot(); // Generate a new carrot
        }
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

        // Draw the carrot if it is visible
        if (carrotVisible && carrotImage != null) {
            g.drawImage(carrotImage, carrotX, carrotY, 50, 50, this); // Draw carrot at its position
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

    public void setCountCarrot() {
        gameManager.setCountCarrot(countCarrot);
    }
    

    private void setCarrotCountText(int countCarrot) {
        jlbcountCorrot.setText(String.valueOf(countCarrot));
        jlbcountCorrot.setBounds(1130, 30, 50, 50);
        jlbcountCorrot.setForeground(Color.BLACK);
        add(jlbcountCorrot); 
    }

    @Override
    public void run() {
        while (true) {
            if (carrotVisible) {
                carrotY += 5; // Move carrot downwards
                if (carrotY > getHeight()) {
                    generateCarrot(); // Regenerate carrot if it goes off screen
                }
                checkCarrotCollection(); // Check if the carrot is collected
                repaint(); // Update the panel with new carrot position
            }
            try {
                Thread.sleep(30); // Control carrot movement speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
