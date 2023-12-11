package com.mycompany.customer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class CustomerPlaceOrder extends JFrame implements ActionListener{
    private static ArrayList<Product> products;
    private JTable menuTable, orderTable;
    private DefaultTableModel menuTableModel, orderTableModel;
    private JTextField totalTextField;
    private JButton checkOutButton;
    private Map<String, Integer> itemQuantityMap = new HashMap<>();
    private Map<String, Double> itemPriceMap = new HashMap<>();
    private String username, userID, contact;
    private double balance;

    public CustomerPlaceOrder(String username, String userID, String contact, double balance, Map<String, Integer> itemQuantityMap, Map<String, Double> itemPriceMap) {
        
        this.itemQuantityMap = itemQuantityMap;
        this.itemPriceMap = itemPriceMap;
        this.username = username;
        this.userID = userID;
        this.contact = contact;
        this.balance = balance;
        
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
        
        // Frame initialization
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        setTitle("Ordering System");
        setSize(900, 300);

        int size = products.size();
        String[][] data = new String[size][2];
        for(int i=0; i<size; i++){
            Product product = products.get(i);
            data[i][0] = product.getName();
            data[i][1] = ""+product.getPrice();
        }
        String[] columnNames = { "Item", "Price" };
        menuTableModel = new DefaultTableModel(data, columnNames);
        menuTable = new JTable(menuTableModel);
        centerAlignTable.centerAlignTable(menuTable); // Center-align the data
        JScrollPane menuScrollPane = new JScrollPane(menuTable);
        add(menuScrollPane, BorderLayout.CENTER);
        
        String[] orderColumns = {"Item", "Single Price", "Decrease", "Amount", "Increase", "Delete"};
        orderTableModel = new DefaultTableModel(null, orderColumns);
        orderTable = new JTable(orderTableModel);
        centerAlignTable.centerAlignTable(orderTable); // Center-align the data
        
        // Customize the column widths
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(5).setPreferredWidth(120);        

        // Set custom cell renderers and editors for the "Increase" and "Decrease" columns
        orderTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));
        orderTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        orderTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        orderTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        
        // Add custom cell renderer and editor for the "Delete" button column
        orderTable.getColumnModel().getColumn(5).setCellEditor(new DeleteButtonEditor(new JCheckBox()));
        orderTable.getColumnModel().getColumn(5).setCellRenderer(new DeleteButtonRenderer());

        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        add(orderScrollPane, BorderLayout.EAST);
        
        
        // Total price display
        totalTextField = new JTextField("Total Price: RM0");
        totalTextField.setEditable(false);
        add(totalTextField, BorderLayout.NORTH);
        
        Font customFont = new Font("Times New Roman", Font.BOLD, 19); // Adjust the font size as needed
        totalTextField.setFont(customFont);
        
        loadOrderedItems();
        calculateTotalPrice();
        
        menuTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedRow = menuTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String selectedItem = (String) menuTableModel.getValueAt(selectedRow, 0); // Get the item name from the selected row

                        // Check if the item already exists in the orderTableModel
                        int quantity = itemQuantityMap.getOrDefault(selectedItem, 0);
                        itemQuantityMap.put(selectedItem, quantity + 1);

                        // Update the order table
                        updateOrderTable();
                        calculateTotalPrice();
                    }
                }
            }
        });

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        
        JButton backButton = new JButton("Back");
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        backButton.addActionListener((ActionEvent e) -> {
            // Go back to the CustomerMainMenu class
            CustomerMainMenu mainMenu = new CustomerMainMenu(username, userID, contact, balance);
            dispose(); // Close the current window
        });
        
        checkOutButton = new JButton("Check Out");
        bottomPanel.add(checkOutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        checkOutButton.addActionListener((ActionEvent e) -> {
            if (orderTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(CustomerPlaceOrder.this, "You have no items in your order. Please add items before checking out.", "Empty Order", JOptionPane.WARNING_MESSAGE);
            } else {
                openCustomerFoodService(); // Open the confirmation GUI when the "Check Out" button is clicked
                // Close the CustomerPlaceOrder window
                dispose();
            }
        });
        
        // Show the frame
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class DeleteButtonRenderer extends JButton implements TableCellRenderer {
        public DeleteButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText("Delete");
            return this;
        }
    }

    // Define a custom cell editor for the "Delete" button column
    class DeleteButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean isPushed;
        private int editingRow;

        public DeleteButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener((ActionEvent e) -> {
                // Set the flag to true when the "Delete" button is clicked
                isPushed = true;
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            editingRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Make sure the row index is valid and exists in the model before attempting to delete
                if (editingRow >= 0 && editingRow < orderTableModel.getRowCount()) {
                    // Use SwingUtilities to ensure that model changes are done on the EDT
                    SwingUtilities.invokeLater(() -> {
                        String itemName = (String) orderTableModel.getValueAt(editingRow, 0);
                        orderTableModel.removeRow(editingRow); // Remove the row from the model
                        itemQuantityMap.remove(itemName); // Remove the item from the map
                        calculateTotalPrice(); // Recalculate the total price
                    });
                }
                isPushed = false;
                // Focus logic remains the same
            }
            return "";
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    
    private void updateOrderTable() {
        orderTableModel.setRowCount(0); // Clear existing rows in the order table
        for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            double price = itemPriceMap.get(itemName);

            // Here we're just adding the String that will be used by the ButtonRenderer and ButtonEditor
            orderTableModel.addRow(new Object[]{itemName, price, "-", quantity, "+"});
        }
    }

    private void calculateTotalPrice() {
        double total = 0;
        for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double price = itemPriceMap.get(itemName);
            total += price * quantity;
        }
        totalTextField.setText("Total Price: RM" + total);
    }
    
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener((ActionEvent e) -> {
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Perform the action depending on which button was clicked
                int row = orderTable.getSelectedRow();
                int col = orderTable.getSelectedColumn();
                String itemName = (String) orderTable.getValueAt(row, 0);
                int quantity = itemQuantityMap.getOrDefault(itemName, 0);

                if (col == 2) {
                    // Decrease button clicked
                    if (quantity > 1) {
                        itemQuantityMap.put(itemName, quantity - 1);
                    }
                } else if (col == 4) {
                    // Increase button clicked
                    itemQuantityMap.put(itemName, quantity + 1);
                }
                
                double price = itemPriceMap.get(itemName);
                orderTableModel.setValueAt(price * quantity, row, 1);
                
                updateOrderTable(); // Update the table model
                calculateTotalPrice(); // Recalculate the total price
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    
    private void openCustomerFoodService() {
        // Create a new instance of CustomerConfirmOrder and pass the selected items and total price
        CustomerFoodService FoodService = new CustomerFoodService(username, userID, contact, itemQuantityMap, itemPriceMap, balance);
        FoodService.setVisible(true); // Show the confirmation GUI
    }
    
    private void loadOrderedItems() {
        for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            double price = itemPriceMap.get(itemName);

            // Here we're just adding the String that will be used by the ButtonRenderer and ButtonEditor
            orderTableModel.addRow(new Object[]{itemName, price, "-", quantity, "+"});
        }
        calculateTotalPrice();
    }
        
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}


