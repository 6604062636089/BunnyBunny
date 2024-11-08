/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bunnybunny;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 *
 * @author This PC
 */
public class StartMenu extends JPanel{
    private Image backgroundImage;
    private JTextField jtfInputName = new JTextField(30);
    private JButton jbtStart = new JButton();
    
    public StartMenu(GameManager gameManager){
        setLayout(null);
        jtfInputName.setBounds(680, 300, 250, 40);//(x, y, width, height)
        jbtStart.setBounds(730, 400, 160, 49);
        
        Border roundedBorder = new LineBorder(Color.BLACK, 3, true); 
        jtfInputName.setHorizontalAlignment(JTextField.CENTER);
        jtfInputName.setBorder(roundedBorder);
        jtfInputName.setOpaque(false);
        
        ImageIcon startIcon = new ImageIcon(getClass().getResource("jbtStart.PNG")); 
        jbtStart.setIcon(startIcon);  
        jbtStart.setHorizontalAlignment(SwingConstants.CENTER); 
        jbtStart.setVerticalAlignment(SwingConstants.CENTER);   
        jbtStart.setContentAreaFilled(false);
        

         jbtStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = jtfInputName.getText().trim();
                if(!playerName.isEmpty()) {
                    gameManager.setPlayerName(playerName);
                    gameManager.showSelectBunny();
                } else {
                    JOptionPane.showMessageDialog(StartMenu.this, "Please enter your name.");
                }
            }
        });
        add(jtfInputName);
        add(jbtStart);
        try {
            backgroundImage = ImageIO.read(getClass().getResource("Bghome.png"));
        } catch (IOException ex) {
            System.out.println("Error loading background image: " + ex.getMessage());
        }
    }
   
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        if(backgroundImage != null){
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }else{
            g.setColor(Color.RED);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
