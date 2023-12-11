package com.mycompany.customer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CustomerMainMenu {
    private JFrame frame;
    private Map<String, Integer> itemQuantityMap = new HashMap<>();
    private Map<String, Double> itemPriceMap = new HashMap<>();
    private String username, userID, contact;
    private double balance;
    
    public CustomerMainMenu(String username, String userID, String contact, double balance) {
        this.username = username;
        this.userID = userID;
        this.contact = contact;
        this.balance = balance;
        
        // Create the main frame
        JFrame frame = new JFrame("Customer Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 100);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Create buttons for Menu, Place Order, Order Status, Order History, and Wallet
        JButton menuButton = new JButton("Menu");
        JButton placeOrderButton = new JButton("Place Order");
        JButton orderStatusButton = new JButton("Order Status");
        JButton orderHistoryButton = new JButton("Order History");
        JButton runnerButton = new JButton("Runner");
        JButton walletButton = new JButton("Wallet");
        JButton LogoutButton = new JButton("Logout");
        
        // Add buttons to the panel
        buttonPanel.add(menuButton);
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(orderStatusButton);
        buttonPanel.add(orderHistoryButton);
        buttonPanel.add(runnerButton);
        buttonPanel.add(walletButton);
        buttonPanel.add(LogoutButton);
                
        // Add the panel to the south region of the frame
        frame.add(buttonPanel, BorderLayout.SOUTH);
        
        placeOrderButton.addActionListener((ActionEvent e) -> {
            // Open the CustomerPlaceOrder page when the button is clicked
            new CustomerPlaceOrder(username, userID, contact, balance, itemQuantityMap, itemPriceMap);
            frame.dispose();
        });
        
        menuButton.addActionListener((ActionEvent e) -> {
            new CustomerViewMenu(username, userID, contact, balance);
            frame.dispose();
        });
        
        runnerButton.addActionListener((ActionEvent e) -> {
            new CustomerReviewRunner(username, userID, contact, balance);
            frame.dispose();
        });
        
        walletButton.addActionListener((ActionEvent e) -> {
            new CustomerWallet(username, userID, contact, balance);
            frame.dispose();
        });
        
        
        frame.setLocationRelativeTo(null);

        // Display the frame
        frame.setVisible(true);
    }
}
