package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import db.MySql;
import java.sql.SQLException;
import java.util.List;

public class OnlineRepairService extends JFrame {
    private final JTable repairTable;
    private DefaultTableModel tableModel;
    private JTextField communityField;
    private JTextField ownerField;
    private MySql mySql;

    public OnlineRepairService() {
        mySql = new MySql();
        setTitle("在线报修服务");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 顶部表单面板
        JPanel topPanel = createFormPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 创建表格
        tableModel = new DefaultTableModel(new Object[]{"小区", "业主", "类别", "描述", "状态"}, 0);
        repairTable = new JTable(tableModel);
        repairTable.setRowHeight(25); // 增加行高提升可读性
        JScrollPane tableScrollPane = new JScrollPane(repairTable);

        // 表格操作按钮面板
        JPanel tableButtonPanel = createTableButtonPanel();

        // 底部面板
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(tableButtonPanel, BorderLayout.SOUTH);
        bottomPanel.add(tableScrollPane, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        // 加载初始数据
        refreshTableFromDatabase();

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 小区名称
        JLabel communityLabel = new JLabel("小区名称:");
        communityField = new JTextField(20);
        addComponent(panel, communityLabel, gbc, 0, 0, GridBagConstraints.EAST);
        addComponent(panel, communityField, gbc, 1, 0, GridBagConstraints.WEST);

        // 业主姓名
        JLabel ownerLabel = new JLabel("业主姓名:");
        ownerField = new JTextField(20);
        addComponent(panel, ownerLabel, gbc, 2, 0, GridBagConstraints.EAST);
        addComponent(panel, ownerField, gbc, 3, 0, GridBagConstraints.WEST);

        // 报修类别
        JLabel typeLabel = new JLabel("报修类别:");
        String[] repairTypes = {"请选择", "水电维修", "设施维修", "其他"};
        JComboBox<String> typeComboBox = new JComboBox<>(repairTypes);
        addComponent(panel, typeLabel, gbc, 0, 1, GridBagConstraints.EAST);
        addComponent(panel, typeComboBox, gbc, 1, 1, GridBagConstraints.WEST);

        // 问题描述
        JLabel descriptionLabel = new JLabel("问题描述:");
        JTextArea descriptionArea = new JTextArea(4, 40);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        panel.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(descriptionScrollPane, gbc);

        // 提交按钮
        JButton submitButton = new JButton("提交");
        submitButton.setPreferredSize(new Dimension(100, 30));
        submitButton.setBackground(new Color(70, 130, 180));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(e -> handleSubmit(
                communityField.getText(),
                ownerField.getText(),
                (String) typeComboBox.getSelectedItem(),
                descriptionArea.getText(),
                communityField,
                ownerField,
                typeComboBox,
                descriptionArea
        ));
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(submitButton, gbc);

        panel.setBorder(BorderFactory.createTitledBorder("提交报修请求"));
        return panel;
    }

    private JPanel createTableButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // 查看全部记录按钮
        JButton viewAllBtn = new JButton("查看全部记录");
        viewAllBtn.addActionListener(e -> {
            refreshTableFromDatabase();
            JOptionPane.showMessageDialog(this, "已从数据库加载最新记录", "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(viewAllBtn);

        // 删除所选按钮
        JButton deleteSelectedBtn = new JButton("删除所选");
        deleteSelectedBtn.addActionListener(e -> deleteSelectedRecord());
        panel.add(deleteSelectedBtn);

        // 标记为完成按钮
        JButton markCompletedBtn = new JButton("标记为完成");
        markCompletedBtn.addActionListener(e -> markAsCompleted());
        panel.add(markCompletedBtn);

        return panel;
    }

    private void handleSubmit(String community, String owner, String type,
                              String description, JTextField communityField,
                              JTextField ownerField, JComboBox<String> typeComboBox,
                              JTextArea descriptionArea) {
        if (validateInput(community, owner, type, description)) {
            try {
                Entity entity = Entity.create("onlinerepair_service")
                        .set("community", community)
                        .set("owner", owner)
                        .set("type", type)
                        .set("description", description)
                        .set("status", "待处理");

                int result = mySql.use().insert(entity);
                if (result > 0) {
                    tableModel.addRow(new Object[]{
                            community,
                            owner,
                            type,
                            description,
                            "待处理"
                    });
                    clearForm(communityField, ownerField, typeComboBox, descriptionArea);
                }
            } catch (SQLException ex) {
                showError("数据库插入错误: " + ex.getMessage());
            }
        } else {
            showError("请填写所有字段并选择报修类型。");
        }
    }

    private boolean validateInput(String community, String owner, String type, String description) {
        return !community.isEmpty() && !owner.isEmpty()
                && !description.isEmpty() && !type.equals("请选择");
    }

    private void clearForm(JTextField communityField, JTextField ownerField,
                           JComboBox<String> typeComboBox, JTextArea descriptionArea) {
        communityField.setText("");
        ownerField.setText("");
        typeComboBox.setSelectedIndex(0);
        descriptionArea.setText("");
    }

    private void markAsCompleted() {
        int selectedRow = repairTable.getSelectedRow();
        if (selectedRow >= 0) {
            String community = (String) tableModel.getValueAt(selectedRow, 0);
            String owner = (String) tableModel.getValueAt(selectedRow, 1);
            String type = (String) tableModel.getValueAt(selectedRow, 2);
            String description = (String) tableModel.getValueAt(selectedRow, 3);

            try {
                String sql = "UPDATE onlinerepair_service SET status = ? WHERE community = ? AND owner = ? AND type = ? AND description = ?";
                int updated = mySql.use().execute(sql, "已完成", community, owner, type, description);

                if (updated > 0) {
                    tableModel.setValueAt("已完成", selectedRow, 4);
                    JOptionPane.showMessageDialog(this, "状态已更新为‘已完成’", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "未找到对应记录，请刷新后重试", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showError("数据库更新错误: " + ex.getMessage());
            }
        } else {
            showError("请选择要标记为完成的行。");
        }
    }

    private void deleteSelectedRecord() {
        int selectedRow = repairTable.getSelectedRow();
        if (selectedRow >= 0) {
            String community = (String) tableModel.getValueAt(selectedRow, 0);
            String owner = (String) tableModel.getValueAt(selectedRow, 1);
            String type = (String) tableModel.getValueAt(selectedRow, 2);
            String description = (String) tableModel.getValueAt(selectedRow, 3);

            try {
                int deleted = mySql.use().del(
                        Entity.create("onlinerepair_service")
                                .set("community", community)
                                .set("owner", owner)
                                .set("type", type)
                                .set("description", description)
                );
                if (deleted > 0) {
                    tableModel.removeRow(selectedRow);
                }
            } catch (SQLException ex) {
                showError("数据库删除错误: " + ex.getMessage());
            }
        } else {
            showError("请选择要删除的行。");
        }
    }

    private void refreshTableFromDatabase() {
        tableModel.setRowCount(0);
        try {
            List<Entity> requests = mySql.use().query(
                    "SELECT * FROM onlinerepair_service ORDER BY community DESC");
            for (Entity request : requests) {
                tableModel.addRow(new Object[]{
                        request.get("community"),
                        request.get("owner"),
                        request.get("type"),
                        request.get("description"),
                        request.get("status")
                });
            }
        } catch (SQLException ex) {
            showError("数据库查询错误: " + ex.getMessage());
        }
    }

    private void addComponent(JPanel panel, Component comp,
                              GridBagConstraints gbc, int x, int y, int anchor) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = anchor;
        panel.add(comp, gbc);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OnlineRepairService service = new OnlineRepairService();
            service.setVisible(true);
        });
    }
}