package com.mycompany.customer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AdminTopUp {

    private static final String BALANCE_FILE_PATH = "Balance.txt";
    private JFrame frame;

    private JTextField userIDTextField;
    private JTextField topUpTextField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new AdminTopUp().initialize();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() throws IOException {
        frame = new JFrame("Admin Top-Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new FlowLayout());
        JLabel userIDLabel = new JLabel("UserID:");
        userIDTextField = new JTextField(10);
        JLabel topUpLabel = new JLabel("Top up amount:");
        topUpTextField = new JTextField(10);

        centerPanel.add(userIDLabel);
        centerPanel.add(userIDTextField);
        centerPanel.add(topUpLabel);
        centerPanel.add(topUpTextField);

        JPanel southPanel = new JPanel();
        JButton backButton = new JButton("Back");
        JButton nextButton = new JButton("Next");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the Back button logic if needed
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performTopUp();
            }
        });

        southPanel.add(backButton);
        southPanel.add(nextButton);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void performTopUp() {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            String enteredCustomerID = userIDTextField.getText().trim();
            double topUpAmount = Double.parseDouble(topUpTextField.getText().trim());

            Map<String, CustomerData> customerDataMap = new HashMap<>();
            reader = new BufferedReader(new FileReader(BALANCE_FILE_PATH));
            String line;
            String currentCustomerID = null;
            String currentCustomerName = null;
            double currentBalance = 0.0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (currentCustomerID == null) {
                    currentCustomerID = line.trim();
                } else if (currentCustomerName == null) {
                    currentCustomerName = line.trim();
                } else {
                    currentBalance = Double.parseDouble(line.trim());
                    customerDataMap.put(currentCustomerID, new CustomerData(currentCustomerName, currentBalance));
                    currentCustomerID = null;
                    currentCustomerName = null;
                }
            }

            if (customerDataMap.containsKey(enteredCustomerID)) {
                CustomerData customerData = customerDataMap.get(enteredCustomerID);
                double newBalance = customerData.getBalance() + topUpAmount;
                customerData.setBalance(newBalance);

                writer = new BufferedWriter(new FileWriter(BALANCE_FILE_PATH));
                for (Map.Entry<String, CustomerData> entry : customerDataMap.entrySet()) {
                    writer.write(entry.getKey() + "\n");
                    writer.write(entry.getValue().getName() + "\n");
                    writer.write(entry.getValue().getBalance() + "\n\n");
                }

                JOptionPane.showMessageDialog(frame, "Top-up successful!");
            } else {
                JOptionPane.showMessageDialog(frame, "CustomerID not found.");
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class CustomerData {
        private String name;
        private double balance;

        public CustomerData(String name, double balance) {
            this.name = name;
            this.balance = balance;
        }

        public String getName() {
            return name;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }
}