package com.mycompany.customer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CustomerWallet extends JFrame {
    private String userID;
    private String username;
    private String contact;
    private double balance;

    // Constructor
    public CustomerWallet(String username, String userID, String contact, double balance) {
        this.userID = userID;
        this.username = username;
        this.contact = contact;
        this.balance = balance;

        setTitle("Customer Wallet");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createGUI();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("UserID:"));
        panel.add(new JLabel(userID));

        panel.add(new JLabel("Username:"));
        panel.add(new JLabel(username));

        panel.add(new JLabel("Balance:"));
        panel.add(new JLabel(String.valueOf(balance)));

        // Create bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CustomerMainMenu(username, userID, contact, balance); // Open the main menu
            }
        });

        bottomPanel.add(backButton);

        // Add bottomPanel to the south of the main panel
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add panel to the center of the main panel
        mainPanel.add(panel, BorderLayout.CENTER);

        add(mainPanel);
    }
}