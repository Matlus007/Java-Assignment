package com.mycompany.customer;
import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CustomerPayment {

    private static final String BALANCE_FILE_PATH = "Balance.txt";

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new CustomerPayment().performPayment("12345", 50.0); // Example UserID and Amount
//        });
//    }

    public void performPayment(String enteredCustomerID, double paymentAmount) {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            Map<String, CustomerWalletData> customerDataMap = new HashMap<>();
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
                    customerDataMap.put(currentCustomerID, new CustomerWalletData(currentCustomerName, currentBalance));
                    currentCustomerID = null;
                    currentCustomerName = null;
                }
            }

            if (customerDataMap.containsKey(enteredCustomerID)) {
                CustomerWalletData customerData = customerDataMap.get(enteredCustomerID);
                double newBalance = customerData.getBalance() + paymentAmount;
                customerData.setBalance(newBalance);

                writer = new BufferedWriter(new FileWriter(BALANCE_FILE_PATH));
                for (Map.Entry<String, CustomerWalletData> entry : customerDataMap.entrySet()) {
                    writer.write(entry.getKey() + "\n");
                    writer.write(entry.getValue().getName() + "\n");
                    writer.write(entry.getValue().getBalance() + "\n\n");
                }

                JOptionPane.showMessageDialog(null, "Payment successful!");
            } else {
                JOptionPane.showMessageDialog(null, "CustomerID not found.");
            }

        } catch (IOException | NumberFormatException e) {
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }
    }
}