package com.mycompany.customer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CustomerFoodService extends JFrame {

    private static final String DINE_IN = "Dine In";
    private static final String TAKE_AWAY = "Take Away";
    private static final String DELIVERY = "Delivery";
    private Map<String, Integer> itemQuantityMap;
    private Map<String, Double> itemPriceMap;
    private String username, userID, contact;
    private double balance;
    private String address = null;

    public CustomerFoodService(String username, String userID, String contact, Map<String, Integer> itemQuantityMap, Map<String, Double> itemPriceMap, double balance) {
        this.itemQuantityMap = itemQuantityMap;
        this.itemPriceMap = itemPriceMap;
        this.username = username;
        this.userID = userID;
        this.contact = contact;
        this.balance = balance;
        
        setTitle("Food Service");
        setSize(400, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.setResizable(false);

        initializeGUI();
    }

    private void initializeGUI() {
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel combinedPanel = createCombinedPanel();

        setLayout(new BorderLayout());
        mainPanel.add(combinedPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createCombinedPanel() {
        JPanel combinedPanel = new JPanel(new GridLayout(2, 1));

        JPanel buttonPanel = createButtonPanel();

        JLabel selectLabel = new JLabel("Select your food service:");

        combinedPanel.add(selectLabel);
        combinedPanel.add(buttonPanel);

        return combinedPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));

        JButton dineInButton = new JButton(DINE_IN);
        JButton takeAwayButton = new JButton(TAKE_AWAY);
        JButton deliveryButton = new JButton(DELIVERY);

        dineInButton.addActionListener(e -> openCustomerConfirmOrder(DINE_IN));
        takeAwayButton.addActionListener(e -> openCustomerConfirmOrder(TAKE_AWAY));
        deliveryButton.addActionListener(this::handleDeliveryButton);

        buttonPanel.add(dineInButton);
        buttonPanel.add(takeAwayButton);
        buttonPanel.add(deliveryButton);

        return buttonPanel;
    }

    private void handleDeliveryButton(ActionEvent e) {
        address = JOptionPane.showInputDialog(this, "Enter your delivery address:");
        if (address != null && !address.isEmpty()) {
            openCustomerConfirmOrder(DELIVERY);
        }
    }
    
    private void openCustomerConfirmOrder(String serviceType) {
        
        CustomerConfirmOrder confirmOrder = new CustomerConfirmOrder(username, userID, contact, address, itemQuantityMap, itemPriceMap, serviceType, balance);
        confirmOrder.setVisible(true); // Show the confirmation GUI
        
        this.dispose();
    }
}