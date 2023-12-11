package com.mycompany.customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.table.JTableHeader;

public class RunnerReviewHandler {
    
    private static final String FILE_NAME = "CustomerReviewRunner.txt";
    private static DefaultTableModel reviewTableModel;

    public static void showReview(String RunnerName, String username, String userID, String contact, double balance) {
        if (userID.startsWith("R")) {
            // Vendor user, use existing code
            showRunnerReview(RunnerName, username, userID, contact, balance);
        } else if (userID.startsWith("Tp")) {
            // Type user, add "Add Review" functionality
            showTypeReview(RunnerName, username, userID, contact, balance);
        }
    }
    
    
    private static void showRunnerReview(String RunnerName, String username, String userID, String contact, double balance) {
        JFrame reviewFrame = new JFrame("Reviews for " + RunnerName);
        reviewFrame.setSize(400, 300);
        reviewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel reviewTableModel = new DefaultTableModel(new String[]{"~ Review ~"}, 0); // Change column name to "Review"
        JTable reviewTable = new JTable(reviewTableModel);

        Font customFont = new Font("Times New Roman", Font.PLAIN, 14); // You can customize the font family, style, and size
        reviewTable.setFont(customFont);

        JScrollPane reviewScrollPane = new JScrollPane(reviewTable);
        reviewFrame.add(reviewScrollPane);

        JTableHeader tableHeader = reviewTable.getTableHeader();
        Font headerFont = new Font("Comic Sans NS", Font.BOLD, 20); // Customize the font family, style, and size for header
        tableHeader.setFont(headerFont);

        // Use the new searchAndAddRows method for loading reviews
        RunnerReviewSearchAndAddRows(RunnerName, reviewTableModel);
        
        // Use the new searchAndAddRows method for loading reviews
        RunnerReviewSearchAndAddRows(RunnerName, reviewTableModel);
        
        JButton backButton = new JButton("Back");

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CustomerReviewRunner(username, userID, contact, balance);
                reviewFrame.dispose();
            }
        });

        reviewFrame.add(bottomPanel, BorderLayout.SOUTH);
        reviewFrame.setLocationRelativeTo(null);
        reviewFrame.setVisible(true);
    }
    
    
    private static void showTypeReview(String RunnerName, String username, String userID, String contact, double balance) {
        JFrame reviewFrame = new JFrame("Reviews for " + RunnerName);
        reviewFrame.setSize(600, 300);
        reviewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel reviewTableModel = new DefaultTableModel(new String[]{"~ Review ~"}, 0); // Change column name to "Review"
        JTable reviewTable = new JTable(reviewTableModel);

        Font customFont = new Font("Times New Roman", Font.PLAIN, 14); // You can customize the font family, style, and size
        reviewTable.setFont(customFont);

        JScrollPane reviewScrollPane = new JScrollPane(reviewTable);
        reviewFrame.add(reviewScrollPane);

        JTableHeader tableHeader = reviewTable.getTableHeader();
        Font headerFont = new Font("Comic Sans MS", Font.BOLD, 20); // Customize the font family, style, and size for header
        tableHeader.setFont(headerFont);

        // Use the new searchAndAddRows method for loading reviews
        RunnerReviewSearchAndAddRows(RunnerName, reviewTableModel);

        // Add review text field and button
        JTextField reviewTextField = new JTextField(20);
        JButton addReviewButton = new JButton("Add Review");
        JButton backButton = new JButton("Back");

        JPanel reviewTextFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Set layout manager
        reviewTextFieldPanel.add(new JLabel("Drop your comment here: "));
        reviewTextFieldPanel.add(reviewTextField);
        reviewTextFieldPanel.add(addReviewButton);
        reviewTextFieldPanel.add(backButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(reviewTextFieldPanel, BorderLayout.CENTER);

        addReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reviewText = reviewTextField.getText();
                if (!reviewText.isEmpty()) {
                    // Add the review to the table model
                    reviewTableModel.addRow(new Object[]{" " + username + ": " + reviewText});
                    
                    RunnerReviewAppendReviewToFile(RunnerName, username, reviewText);

                    // Clear the review text field
                    reviewTextField.setText("");
                }
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CustomerReviewRunner(username, userID, contact, balance);
                reviewFrame.dispose();
            }
        });

        reviewFrame.add(bottomPanel, BorderLayout.SOUTH);

        reviewFrame.setLocationRelativeTo(null);
        reviewFrame.setVisible(true);
    }

    private static void RunnerReviewSearchAndAddRows(String RunnerName, DefaultTableModel reviewTableModel) {
        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {
            String currentitemName = null;
            String user = null;
            StringBuilder reviewTextBuilder = new StringBuilder();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    // End of a block, add data to the model if itemName matches
                    if (currentitemName != null && currentitemName.equals(RunnerName)) {
                        reviewTableModel.addRow(new Object[]{" " + user + ": " + reviewTextBuilder.toString().trim()});
                    }

                    // Reset variables for the next block
                    currentitemName = null;
                    user = null;
                    reviewTextBuilder = new StringBuilder();
                } else {
                    // Read itemName, username, and reviewText
                    if (currentitemName == null) {
                        currentitemName = line.trim();
                    } else if (user == null) {
                        user = line.trim();
                    } else {
                        reviewTextBuilder.append(line.trim()).append("\n");
                    }
                }
            }

            // Check for the last block at the end of the file
            if (currentitemName != null && currentitemName.equals(RunnerName)) {
                reviewTableModel.addRow(new Object[]{" " + user + ": " + reviewTextBuilder.toString().trim()});
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static void RunnerReviewAppendReviewToFile(String RunnerName, String username, String reviewText) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                // Append the item name, username, and reviewText to the file
                writer.write(RunnerName);
                writer.newLine();
                writer.write(username);
                writer.newLine();
                writer.write(reviewText);
                writer.newLine();
                writer.newLine(); // Add an empty line to separate reviews

                // Display a success message
                System.out.println("Review added to file successfully.");

            } catch (IOException ex) {
                ex.printStackTrace();
                // Handle the exception (e.g., display an error message to the user)
        }
    }
}