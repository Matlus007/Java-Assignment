package com.mycompany.customer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class CustomerConfirmOrder extends JFrame {
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private Map<String, Integer> itemQuantityMap;
    private Map<String, Double> itemPriceMap;
    private String username, userID, address, contact, serviceType;
    private double balance;
    private boolean payment;

    public CustomerConfirmOrder(String username, String userID, String contact, String address, Map<String, Integer> itemQuantityMap, Map<String, Double> itemPriceMap, String serviceType, double balance) {
        this.itemQuantityMap = itemQuantityMap;
        this.itemPriceMap = itemPriceMap;
        this.username = username;
        this.userID = userID;
        this.contact = contact;
        this.address = address;
        this.serviceType = serviceType;
        this.balance = balance;

        setTitle("Confirm Order");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel to hold the order table
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);
        
        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Create "Modify Order" button
        JButton modifyOrderButton = new JButton("Modify Order");
        buttonPanel.add(modifyOrderButton);

        // Create "Make Payment" button
        JButton makePaymentButton = new JButton("Make Payment");
        buttonPanel.add(makePaymentButton);
        
        CustomerOrderSucess orderSuccess = new CustomerOrderSucess(itemQuantityMap, itemPriceMap, serviceType);

        // Action listener for "Modify Order" button
        modifyOrderButton.addActionListener((ActionEvent e) -> {
            // Close the current CustomerConfirmOrder window
            dispose();

            // Reopen the CustomerPlaceOrder window with the existing ordered items
            SwingUtilities.invokeLater(() -> new CustomerPlaceOrder(username, userID, contact, balance, itemQuantityMap, itemPriceMap));
        });

        // Action listener for "Make Payment" button
        makePaymentButton.addActionListener((ActionEvent e) -> {
            
            if (paymentStatus(balance)){
                double NewBalance = makePayment(balance);
                
                new CustomerPayment().performPayment(userID, NewBalance);
                String orderId = orderSuccess.generateOrderId(serviceType);
                orderSuccess.saveOrderToFile(username, userID, contact, orderId, address, serviceType);

                CustomerPaymentSuccessGUI paymentSuccessGUI = new CustomerPaymentSuccessGUI();
                // Display a success message with the generated order ID
                paymentSuccessGUI.showOrderSuccessMessage(username, userID, contact, orderId, NewBalance);
                dispose();
            }else{
                String warningMessage = "Your balance is not enough. (Current Balance = RM" + balance + ")";
                new CustomerWarningDialog(warningMessage, CustomerConfirmOrder.this);
            }
        });

        // Create the table model for the order details
        String[] columnNames = {"Item", "Single Price", "Amount"};
        orderTableModel = new DefaultTableModel(null, columnNames);
        orderTable = new JTable(orderTableModel);

        // Customize the column widths
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        // Center-align the data in the table
        centerAlignTable.centerAlignTable(orderTable);

        // Populate the order table with the selected items and quantities
        for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double price = itemPriceMap.get(itemName);

            orderTableModel.addRow(new Object[]{itemName, price, quantity});
        }

        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        panel.add(orderScrollPane, BorderLayout.CENTER);
        
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel foodServiceLabel = new JLabel(" Food Service: " + serviceType);
        foodServiceLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        foodServiceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        JPanel labelsPanel = new JPanel(new BorderLayout());
        labelsPanel.add(foodServiceLabel, BorderLayout.NORTH);
        topPanel.add(labelsPanel, BorderLayout.NORTH);
        
        if (address != null) {
            JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel addressLabel = new JLabel("Address: " + address);
            addressLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
            addressPanel.add(addressLabel);
            topPanel.add(addressPanel, BorderLayout.CENTER);
        }

        double totalPrice = calculateTotalPrice();
        JLabel totalLabel = new JLabel(" Total Price: RM" + totalPrice);
        totalLabel.setFont(new Font("Times New Roman", Font.BOLD, 19));
        totalLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(totalLabel, BorderLayout.SOUTH);
        panel.add(topPanel, BorderLayout.NORTH);
        
        setVisible(true);
    }

    private double calculateTotalPrice() {
        double total = 0;
        for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double price = itemPriceMap.get(itemName);
            total += price * quantity;
        }
        return total;
    }
    
    private double makePayment(double balance) {
        double total = calculateTotalPrice();
        // Check if the balance is enough for the order
        if (balance >= total) {
            balance -= total;
        }
        return balance;
    }
    
    private boolean paymentStatus(double balance){
        double tempbalance = balance;
        balance = makePayment(balance);
        
        payment = balance < tempbalance;
        
        return payment;
    }
}