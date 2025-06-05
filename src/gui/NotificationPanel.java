package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;

public class NotificationPanel extends JFrame {
    private JTextArea messageArea;
    private JTextField titleField;
    private JCheckBox urgentCheckBox;
    private DefaultTableModel tableModel;
    private JTable statusTable;
    private JButton sendButton;
    private JTextField customCommunityField;

    public NotificationPanel() {
        setTitle("通知系统");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 面板用于显示通知详细信息
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题字段
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("标题:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        titleField = new JTextField(20);
        detailsPanel.add(titleField, gbc);

        // 消息区域
        gbc.gridx = 0;
        gbc.gridy = 1;
        detailsPanel.add(new JLabel("消息:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        messageArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        detailsPanel.add(scrollPane, gbc);

        // 小区字段
        gbc.gridx = 0;
        gbc.gridy = 2;
        detailsPanel.add(new JLabel("小区:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        customCommunityField = new JTextField(20);
        detailsPanel.add(customCommunityField, gbc);

        // 紧急复选框
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        urgentCheckBox = new JCheckBox("紧急通知");
        detailsPanel.add(urgentCheckBox, gbc);

        add(detailsPanel, BorderLayout.NORTH);

        // 表格用于显示通知状态和反馈
        String[] columnNames = {"小区", "标题", "消息"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 使表格不可编辑
            }
        };
        statusTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(statusTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // 发送按钮
        sendButton = new JButton("发送通知");
        sendButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String message = messageArea.getText().trim();
            String community = customCommunityField.getText().trim();

            if (title.isEmpty() || message.isEmpty() || community.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "请填写所有字段",
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // 插入到数据库
                PropCore.INS.getMySql().use().insert(
                        Entity.create("notification_panel")
                                .set("community", community)
                                .set("title", title)
                                .set("message", message)
                );

                // 清空字段
                titleField.setText("");
                messageArea.setText("");
                customCommunityField.setText("");

                // 刷新表格
                loadNotifications();

                JOptionPane.showMessageDialog(this,
                        "通知发送成功",
                        "成功",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "保存通知时出错: " + ex.getMessage(),
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonsPanel.add(sendButton);

        // 删除按钮
        JButton deleteButton = new JButton("删除所选");
        deleteButton.addActionListener(e -> {
            int selectedRow = statusTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "请选择要删除的通知",
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String community = (String) tableModel.getValueAt(selectedRow, 0);
            String title = (String) tableModel.getValueAt(selectedRow, 1);

            try {
                // 从数据库中删除
                int deleted = PropCore.INS.getMySql().use().del(
                        Entity.create("notification_panel")
                                .set("community", community)
                                .set("title", title)
                );

                if (deleted > 0) {
                    loadNotifications();
                    JOptionPane.showMessageDialog(this,
                            "通知删除成功",
                            "成功",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "未在数据库中找到通知",
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "删除通知时出错: " + ex.getMessage(),
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonsPanel.add(deleteButton);

        // 刷新按钮
        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadNotifications());
        buttonsPanel.add(refreshButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        // 加载初始数据
        loadNotifications();
    }

    /**
     * 从数据库加载通知并在表格中显示
     */
    private void loadNotifications() {
        try {
            List<Entity> records = PropCore.INS.getMySql().use().findAll("notification_panel");
            tableModel.setRowCount(0); // 清除现有数据

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
                    "加载通知时出错: " + e.getMessage(),
                    "错误",
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