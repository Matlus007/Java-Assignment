package com.mycompany.customer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

public class CustomerWarningDialog extends JDialog {
    public CustomerWarningDialog(String warningMessage, JFrame parent) {
        super(parent, "Warning", true);

        setSize(400, 100);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        
        // Explicitly set text alignment to center
        JLabel messageLabel = new JLabel(warningMessage, JLabel.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener((ActionEvent e) -> {
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(panel);

        setLocationRelativeTo(parent);
        setVisible(true);
    }
}