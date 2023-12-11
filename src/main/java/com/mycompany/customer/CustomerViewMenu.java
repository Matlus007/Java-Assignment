package com.mycompany.customer;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CustomerViewMenu extends JFrame {
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private static ArrayList<Product> products;
    private Map<String, Double> itemPriceMap = new HashMap<>();
    private String username, userID, contact;
    private double balance;
    
    public CustomerViewMenu(String username, String userID, String contact, double balance) {
        this.username = username;
        this.userID = userID;
        this.balance = balance;
        
        setTitle("Customer View Menu");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        tableModel = new DefaultTableModel(new String[]{"Item", "Price", "Review"}, 0);
        menuTable = new JTable(tableModel);
        menuTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        menuTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));
        centerAlignTable.centerAlignTable(menuTable);

        JScrollPane scrollPane = new JScrollPane(menuTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener((ActionEvent e) -> {
            new CustomerMainMenu(username, userID, contact, balance);
            dispose();
        });
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);

        loadMenuItems("products.txt");

        menuTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = menuTable.getColumnModel().getColumnIndex("Review");
                int row = menuTable.rowAtPoint(e.getPoint());

                if (column != -1 && row != -1) {
                    showReviews(row);
                }
                dispose();
            }
        });
    }

    private void loadMenuItems(String fileName) {
        try {
            // Load product data from a file (modify the file name as needed)
            Scanner scanner = new Scanner(new File("products.txt"));
            products = new ArrayList<>();
            while (scanner.hasNext()) {
                String name = scanner.nextLine();
                double price = Double.parseDouble(scanner.nextLine());
                scanner.nextLine();
                Product product = new Product(name, price);
                products.add(product);
                itemPriceMap.put(name, price);
            }
            scanner.close();
        } catch (FileNotFoundException | NumberFormatException e) {
            products = new ArrayList<>();
        }

        for (Product product : products) {
            JButton reviewButton = new JButton("Review");
            reviewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = menuTable.getSelectedRow();
                    if (row != -1) {
                        showReviews(row);
                    }
                }
            });
            tableModel.addRow(new Object[]{product.getName(), product.getPrice(), reviewButton});
        }
        menuTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        menuTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));
    }
    
    private void showReviews(int selectedRow) {
        String itemName = (String) tableModel.getValueAt(selectedRow, 0);
        ReviewHandler.showReviews(itemName, username, userID, contact, balance);
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

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                int selectedRow = menuTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedRow = menuTable.convertRowIndexToModel(selectedRow);
                    showReviews(selectedRow);
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