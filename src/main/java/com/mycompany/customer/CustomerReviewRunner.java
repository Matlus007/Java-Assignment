package com.mycompany.customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EventObject;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomerReviewRunner extends JFrame {

    private JTable table;
    private String username, userID, contact;
    private double balance;
    
    public JTable getTable() {
        return table;
    }

    public CustomerReviewRunner(String username, String userID, String contact, double balance) {
        this.username = username;
        this.userID = userID;
        this.balance = balance;
        
        setTitle("Customer Review Runner");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the table model with columns: RunnerID, RunnerName, Review
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("RunnerID");
        tableModel.addColumn("RunnerName");
        tableModel.addColumn("Review");

        // Create the table using the table model
        table = new JTable(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener((ActionEvent e) -> {
            new CustomerMainMenu(username, userID, contact, balance);
            dispose();
        });
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Read the User.txt file and populate the table
        readUserFileAndPopulateTable();
        
        setLocationRelativeTo(null);
        
        // Set up the GUI
        setVisible(true);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.getColumnModel().getColumnIndex("Review");
                int row = table.rowAtPoint(e.getPoint());

                if (column != -1 && row != -1) {
                    showReview(row);
                }
                dispose();
            }
        });
    }

    private void readUserFileAndPopulateTable() {
        try (BufferedReader br = new BufferedReader(new FileReader("User.txt"))) {
            String line;
            String runnerID = "";
            String runnerName = "";

            while ((line = br.readLine()) != null) {
                if (line.trim().equals("")) {
                    // Skip empty lines
                    continue;
                }

                if (line.equals("Runner")) {
                    // Read the next two lines to get RunnerID and RunnerName
                    runnerID = br.readLine();
                    runnerName = br.readLine();

                    // Display the information in the table
                    displayRunnerInfo(runnerID, runnerName);
                }
            }
        } catch (IOException e) {
        }
        
        // Set custom renderer and editor for the "Review" column
        int reviewColumnIndex = 2;
        table.getColumnModel().getColumn(reviewColumnIndex).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(reviewColumnIndex).setCellEditor(new ButtonEditor());
    }

    private void displayRunnerInfo(String runnerID, String runnerName) {
        // Add a new row to the table with RunnerID, RunnerName, and a Review button
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{runnerID, runnerName, new JButton("Review")});
        centerAlignTable.centerAlignTable(table);
    }
    
    private void showReview(int selectedRow) {
        String RunnerName = (String) table.getValueAt(selectedRow, 1);
        RunnerReviewHandler.showReview(RunnerName, username, userID, contact, balance);
    }

    class ButtonRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Component) {
                return (Component) value;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;

        public ButtonEditor() {
            super(new JCheckBox());
            button = new JButton("Review");
            button.setOpaque(true);
            button.addActionListener((ActionEvent e) -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    selectedRow = table.convertRowIndexToModel(selectedRow);
                    showReview(selectedRow);
                }
            });
        }
        
        @Override
        public boolean isCellEditable(EventObject anEvent) {
            // Override this method to make the cell non-editable
            return false;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof Component) {
                button.setText(((JButton) value).getText()); // Set the text of the JButton
                return (Component) value;
            }
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        @Override
        public Object getCellEditorValue() {
            return button;
        }
    }
}