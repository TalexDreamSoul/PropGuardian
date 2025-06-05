package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import db.MySql;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;

public class NotificationPanel extends JFrame {
    private JTextArea messageArea;
    private JTextField titleField;
    private JCheckBox urgentCheckBox;
    private DefaultTableModel tableModel;
    private JTable statusTable;
    private JButton sendButton;
    private MySql mySql;
    private JTextField customCommunityField;

    public NotificationPanel() {
        mySql = new MySql(); // Initialize database connection
        setTitle("Notification System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel for notification details
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title field
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        titleField = new JTextField(20);
        detailsPanel.add(titleField, gbc);

        // Message area
        gbc.gridx = 0;
        gbc.gridy = 1;
        detailsPanel.add(new JLabel("Message:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        messageArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        detailsPanel.add(scrollPane, gbc);

        // Community field
        gbc.gridx = 0;
        gbc.gridy = 2;
        detailsPanel.add(new JLabel("Community:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        customCommunityField = new JTextField(20);
        detailsPanel.add(customCommunityField, gbc);

        // Urgent checkbox
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        urgentCheckBox = new JCheckBox("Urgent Notification");
        detailsPanel.add(urgentCheckBox, gbc);

        add(detailsPanel, BorderLayout.NORTH);

        // Table for notification status and feedback
        String[] columnNames = {"Community", "Title", "Message"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        statusTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(statusTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Send button
        sendButton = new JButton("Send Notification");
        sendButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String message = messageArea.getText().trim();
            String community = customCommunityField.getText().trim();

            if (title.isEmpty() || message.isEmpty() || community.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Insert into database
                mySql.use().insert(
                        Entity.create("notification_panel")
                                .set("community", community)
                                .set("title", title)
                                .set("message", message)
                );

                // Clear fields
                titleField.setText("");
                messageArea.setText("");
                customCommunityField.setText("");

                // Refresh table
                loadNotifications();

                JOptionPane.showMessageDialog(this,
                        "Notification sent successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error saving notification: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonsPanel.add(sendButton);

        // Delete button
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> {
            int selectedRow = statusTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a notification to delete",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String community = (String) tableModel.getValueAt(selectedRow, 0);
            String title = (String) tableModel.getValueAt(selectedRow, 1);

            try {
                // Delete from database
                int deleted = mySql.use().del(
                        Entity.create("notification_panel")
                                .set("community", community)
                                .set("title", title)
                );

                if (deleted > 0) {
                    loadNotifications();
                    JOptionPane.showMessageDialog(this,
                            "Notification deleted successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Notification not found in database",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error deleting notification: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonsPanel.add(deleteButton);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadNotifications());
        buttonsPanel.add(refreshButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        // Load initial data
        loadNotifications();
    }

    /**
     * Loads notifications from the database and displays them in the table
     */
    private void loadNotifications() {
        try {
            List<Entity> records = mySql.use().findAll("notification_panel");
            tableModel.setRowCount(0); // Clear existing data

            for (Entity record : records) {
                tableModel.addRow(new Object[]{
                        record.getStr("community"),
                        record.getStr("title"),
                        record.getStr("message")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading notifications: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NotificationPanel panel = new NotificationPanel();
            panel.setVisible(true);
        });
    }
}