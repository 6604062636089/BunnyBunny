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
    private Image bombImage; 
    private Image heartIcon;
    private int countCarrot = 0;
    private int lifeCount = 3;
    int x = 550; 
    int y = 450; 
    int moveAmount = 10; 
    private boolean positionChanged = false;
    private JLabel jlbcountCorrot = new JLabel();
    private volatile boolean running = true;

    private JLabel countdownLabel = new JLabel(); 
    private int countdownTime = 75; 
    private Timer countdownTimer; 

    private int carrotX;
    private int carrotY;
    private boolean carrotVisible = false;

    private int bombX; 
    private int bombY; 
    private boolean bombVisible = false; 
    private Random random = new Random();

    public GamePlayPanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerName = gameManager.getPlayerName();
        setLayout(null);

        setFocusable(true);

        backgroundImage = new ImageIcon(getClass().getResource("BgGame.png")).getImage();
        startIcon = new ImageIcon(getClass().getResource("StartIcon.png")).getImage();
        startIconBounds = new Rectangle(400, 200, 400, 250);
        bunny1Image = new ImageIcon(getClass().getResource("bunny1.png")).getImage();
        bunny2Image = new ImageIcon(getClass().getResource("bunny2.png")).getImage();
        heartIcon = new ImageIcon(getClass().getResource("heart1.png")).getImage();
        carrotImage = new ImageIcon(getClass().getResource("carrot.png")).getImage(); 
        bombImage = new ImageIcon(getClass().getResource("bomb.png")).getImage(); 
        
        jlbplayName.setText(playerName);
        jlbplayName.setBounds(980, 3, 1000, 100);
        jlbplayName.setForeground(Color.BLACK);
        add(jlbplayName);

        countdownLabel.setText("Time Left: " + formatTime(countdownTime));
        countdownLabel.setBounds(10, 65, 200, 50);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 20));
        countdownLabel.setForeground(Color.RED);
        add(countdownLabel);

        countdownTimer = new Timer(1000, e -> {
            countdownTime--;
            countdownLabel.setText("Time Left: " + formatTime(countdownTime));
            if (countdownTime <= 0) {
                countdownTimer.stop();
                gameManager.setCountCarrot(countCarrot);
                gameManager.showGameEnd();
                running = false; 
                lifeCount -= 1;
                System.out.println("Time Out!");
            }
        });

        generateCarrot();

        Thread carrotThread = new Thread(this);
        Thread bombThread = new Thread(new BombFallingRunnable());

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                positionChanged = false; 
                if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
                    x -= moveAmount;
                    positionChanged = true; 
                } else if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
                    x += moveAmount;
                    positionChanged = true;
                }
                x = Math.max(0, Math.min(x, getWidth() - 135));
                if (positionChanged) {
                    //System.out.println("Bunny moved to x: " + x + ", y: " + y);
                }
                checkCarrotCollection(); 
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickCounter == 0) {
                    requestFocusInWindow();
                    clickCounter += 1;
                    startIcon = null;
                    countdownTimer.start(); 
                    carrotThread.start();
                    bombThread.start();
                }
            }
        });

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

    private void generateCarrot() {
        int panelWidth = getWidth();
        if (panelWidth <= 50) {  
            carrotX = 550; 
        } else {
            carrotX = random.nextInt(panelWidth - 50);
        }
        carrotY = 0;
        carrotVisible = true; 
    }

    private void generateBomb() {
        int panelWidth = getWidth();
        if (panelWidth <= 50) {
            bombX = 0; 
        } else {
            bombX = random.nextInt(panelWidth - 50); 
        }
        bombY = 0; 
        bombVisible = true; 
    }

    private void checkCarrotCollection() {
        Rectangle bunnyRect = new Rectangle(x, y, 80, 180);
        Rectangle carrotRect = new Rectangle(carrotX, carrotY, 25, 25); 
        if (bunnyRect.intersects(carrotRect)) {
            countCarrot += 1;
            System.out.println("Carrot collected!");
            System.out.println("Your scores : " + countCarrot);
            carrotVisible = false; 
            setCarrotCountText(countCarrot);
            generateCarrot(); 
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
                running = false; 
                System.out.println("Bomb collected! Game Over!");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (startIcon != null) {
            g.drawImage(startIcon, startIconBounds.x, startIconBounds.y, startIconBounds.width, startIconBounds.height, this);
        }

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

        if (carrotVisible && carrotImage != null) {
            g.drawImage(carrotImage, carrotX, carrotY, 50, 50, this); // Draw carrot at its position
        }

        if (bombVisible && bombImage != null) {
            g.drawImage(bombImage, bombX, bombY, 50, 50, this); // Draw bomb at its position
        }
    }
    
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
                carrotY += 5; 
                if (carrotY > getHeight()) {
                    generateCarrot(); 
                }
                checkCarrotCollection(); 
                repaint(); 
            }
            try {
                Thread.sleep(30); 
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
                    Thread.sleep(2000); 
                    generateBomb();
                    while (bombVisible) {
                        bombY += 5;  
                        if (bombY > getHeight()) {
                            bombVisible = false; 
                        }
                        checkBombCollection(); 
                        repaint(); 
                        Thread.sleep(30); 
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
