package bunnybunny;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePlayPanel extends JPanel implements Runnable {

    private GameManager gameManager;
    private Image startIcon;
    private Rectangle startIconBounds;
    private String playerName = "player";
    private int selectedBunny;
    private int clickCounter = 0;
    private Image backgroundImage;
    private JLabel jlbplayName = new JLabel();
    private Image bunny1Image;
    private Image bunny2Image;
    private Image carrotImage;
    private Image bombImage; // Image for the bomb
    private Image heartIcon;
    private int countCarrot = 0;
    private int lifeCount = 3;
    int x = 550; // Initial x position of the bunny
    int y = 450; // Initial y position of the bunny
    int moveAmount = 10; // Number of pixels to move per key press
    private boolean positionChanged = false;
    private JLabel jlbcountCorrot = new JLabel();
    private volatile boolean running = true;

    private JLabel countdownLabel = new JLabel(); // Label for countdown timer
    private int countdownTime = 75; // Initial countdown time in seconds (1 minute 15 seconds)
    private Timer countdownTimer; // Swing Timer for countdown

    // Carrot properties
    private int carrotX;
    private int carrotY;
    private boolean carrotVisible = false;

    private int bombX; // X position of the bomb
    private int bombY; // Y position of the bomb
    private boolean bombVisible = false; // Bomb visibility
    private Random random = new Random();

    public GamePlayPanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerName = gameManager.getPlayerName(); // Retrieve player name from GameManager
        setLayout(null);

        // Set focusable and request focus
        setFocusable(true);

        // Load images
        backgroundImage = new ImageIcon(getClass().getResource("BgGame.png")).getImage();
        startIcon = new ImageIcon(getClass().getResource("StartIcon.png")).getImage();
        startIconBounds = new Rectangle(400, 200, 400, 250);
        bunny1Image = new ImageIcon(getClass().getResource("bunny1.png")).getImage();
        bunny2Image = new ImageIcon(getClass().getResource("bunny2.png")).getImage();
        heartIcon = new ImageIcon(getClass().getResource("heart1.png")).getImage();
        carrotImage = new ImageIcon(getClass().getResource("carrot.png")).getImage(); // Load carrot image
        bombImage = new ImageIcon(getClass().getResource("bomb.png")).getImage(); // Load bomb image

        // Display player name
        jlbplayName.setText(playerName);
        jlbplayName.setBounds(980, 3, 1000, 100);
        jlbplayName.setForeground(Color.BLACK);
        add(jlbplayName);

        // Display countdown timer
        countdownLabel.setText("Time Left: " + formatTime(countdownTime));
        countdownLabel.setBounds(10, 65, 200, 50);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 20));
        countdownLabel.setForeground(Color.RED);
        add(countdownLabel);

        // Timer to handle countdown
        countdownTimer = new Timer(1000, e -> {
            countdownTime--;
            countdownLabel.setText("Time Left: " + formatTime(countdownTime));
            if (countdownTime <= 0) {
                countdownTimer.stop();
                gameManager.setCountCarrot(countCarrot);
                gameManager.showGameEnd();
                running = false; // Stop the threads
                lifeCount -= 1;
                System.out.println("Time Out!");
            }
        });

        // Generate initial carrot position
        generateCarrot();

        // Start thread for falling carrot
        Thread carrotThread = new Thread(this);
        Thread bombThread = new Thread(new BombFallingRunnable());
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
//                    System.out.println("Bunny moved to x: " + x + ", y: " + y);
                }
                checkCarrotCollection(); // Check for carrot collection
                repaint();
            }
        });

        // Add mouse listener for focus
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickCounter == 0) {
                    requestFocusInWindow(); // Request focus on mouse click
                    clickCounter += 1;
                    startIcon = null;
                    countdownTimer.start(); // Start countdown timer
                    carrotThread.start();
                    bombThread.start();
                }
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

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    // Generate carrot at a random position
    private void generateCarrot() {
        int panelWidth = getWidth();
        if (panelWidth <= 50) {
            // Handle case when panel width is too small
            carrotX = 550; // Position carrot at the left edge
        } else {
            carrotX = random.nextInt(panelWidth - 50); // Random x position for carrot
        }
        carrotY = 0; // Start from the top of the panel
        carrotVisible = true; // Set carrot to visible
    }

    private void generateBomb() {
        int panelWidth = getWidth();
        if (panelWidth <= 50) {
            bombX = 0; // Position bomb at the left edge
        } else {
            bombX = random.nextInt(panelWidth - 50); // Random x position for bomb
        }
        bombY = 0; // Start from the top of the panel
        bombVisible = true; // Set bomb to visible
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

    private void checkBombCollection() {
        Rectangle bunnyRect = new Rectangle(x, y, 80, 180);
        Rectangle bombRect = new Rectangle(bombX, bombY, 25, 25);
        if (bunnyRect.intersects(bombRect)) {
            if (lifeCount != 1) {
                lifeCount -= 1;
                bombVisible = false;
                System.out.println("life count : " + lifeCount);
            } else {
                gameManager.setCountCarrot(countCarrot);
                gameManager.showGameEnd();
                running = false; // Stop the threads
                System.out.println("Bomb collected! Game Over!");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (startIcon != null) {
            g.drawImage(startIcon, startIconBounds.x, startIconBounds.y, startIconBounds.width, startIconBounds.height, this);
        }

        // Draw the selected bunny at the updated position
        if (selectedBunny == 1 && bunny1Image != null) {
            g.drawImage(bunny1Image, x, y, 135, 290, this);
        } else if (selectedBunny == 2 && bunny2Image != null) {
            g.drawImage(bunny2Image, x, y, 135, 290, this);
        }

        if (lifeCount == 3 && heartIcon != null) {
            g.drawImage(heartIcon, 12, 25, 50, 50, this);
            g.drawImage(heartIcon, 62, 25, 50, 50, this);
            g.drawImage(heartIcon, 112, 25, 50, 50, this);
        } else if (lifeCount == 2 && heartIcon != null) {
            g.drawImage(heartIcon, 12, 25, 50, 50, this);
            g.drawImage(heartIcon, 62, 25, 50, 50, this);
        } else if (lifeCount == 1 && heartIcon != null) {
            g.drawImage(heartIcon, 12, 25, 50, 50, this);
        }

        // Draw the carrot if it is visible
        if (carrotVisible && carrotImage != null) {
            g.drawImage(carrotImage, carrotX, carrotY, 50, 50, this); // Draw carrot at its position
        }

        // Draw the bomb if it is visible
        if (bombVisible && bombImage != null) {
            g.drawImage(bombImage, bombX, bombY, 50, 50, this); // Draw bomb at its position
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
        jlbcountCorrot.setBounds(1120, 30, 50, 50);
        jlbcountCorrot.setForeground(Color.BLACK);
        jlbcountCorrot.setFont(new Font("Arial", Font.BOLD, 35));
        add(jlbcountCorrot);
    }

    @Override
    public void run() {
        while (running) {
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

    private class BombFallingRunnable implements Runnable {

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(2000); // Wait for 2 seconds to generate a bomb
                    generateBomb(); // Generate a new bomb every 2 seconds
                    while (bombVisible) {
                        bombY += 5; // Move bomb downwards
                        if (bombY > getHeight()) {
                            bombVisible = false; // Hide bomb if it goes off screen
                        }
                        checkBombCollection(); // Check if the bomb is collected
                        repaint(); // Update the panel with new bomb position
                        Thread.sleep(30); // Control bomb movement speed
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
