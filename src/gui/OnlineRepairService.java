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

public class OnlineRepairService extends JFrame {
    private final JTable repairTable;
    private DefaultTableModel tableModel;
    private JTextField communityField;
    private JTextField ownerField;
    private MySql mySql; // 新增：数据库连接实例

    public   OnlineRepairService() {
        mySql = new MySql(); // 初始化数据库连接
        setTitle("Online Repair Service");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create top panel with form for submitting repair requests
        JPanel topPanel = new JPanel();
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        topPanel.setLayout(gridBag);

        // 设置内边距
        constraints.insets = new Insets(5, 5, 5, 5);

        // 小区名称标签和输入框
        JLabel communityLabel = new JLabel("Community:");
        communityField = new JTextField(20);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        topPanel.add(communityLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        topPanel.add(communityField, constraints);

        // 业主姓名标签和输入框
        JLabel ownerLabel = new JLabel("Owner:");
        ownerField = new JTextField(20);

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        topPanel.add(ownerLabel, constraints);

        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        topPanel.add(ownerField, constraints);

        // 报修类别标签和下拉框
        JLabel typeLabel = new JLabel("Repair Type:");
        String[] repairTypes = {"请选择", "水电维修", "设施维修", "其他"};
        JComboBox<String> typeComboBox = new JComboBox<>(repairTypes);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.EAST;
        topPanel.add(typeLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        topPanel.add(typeComboBox, constraints);

        // 问题描述标签和文本域
        JLabel descriptionLabel = new JLabel("Problem Description:");

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        topPanel.add(descriptionLabel, constraints);

        JTextArea descriptionArea = new JTextArea(4, 40);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(descriptionScrollPane, constraints);

        // 提交按钮
        JButton submitButton = new JButton("Submit");

        constraints.gridx = 4;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        topPanel.add(submitButton, constraints);

        // 添加表单到顶部面板
        topPanel.setBorder(BorderFactory.createTitledBorder("Submit Repair Request"));
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 创建表格模型和表格
        tableModel = new DefaultTableModel(new Object[]{"ID", "Community", "Owner", "Type", "Description", "Status"}, 0);
        repairTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(repairTable);

        // 表格操作按钮面板
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // 删除所选按钮
        JButton deleteSelectedBtn = new JButton("Delete Selected");
        deleteSelectedBtn.addActionListener(e -> {
            int selectedRow = repairTable.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(OnlineRepairService.this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        tableButtonPanel.add(deleteSelectedBtn);

        // 标记为完成按钮
        JButton markCompletedBtn = new JButton("Mark as Completed");
        markCompletedBtn.addActionListener(e -> {
            int selectedRow = repairTable.getSelectedRow();
            if (selectedRow >= 0) {
                // 更新界面状态
                tableModel.setValueAt("Completed", selectedRow, 5);

                // 获取选中的行数据
                String community = tableModel.getValueAt(selectedRow, 1).toString();
                String owner = tableModel.getValueAt(selectedRow, 2).toString();
                String type = tableModel.getValueAt(selectedRow, 3).toString();
                String description = tableModel.getValueAt(selectedRow, 4).toString();

                try {
                    // 更新数据库状态
                    mySql.use().update(
                        Entity.create("onlinerepair_service")
                            .set("status", "Completed"),
                        Entity.create("onlinerepair_service")
                            .set("community", community)
                            .set("owner", owner)
                            .set("type", type)
                            .set("description", description)
                            .set("status", "Pending") // 假设初始状态为"Pending"
                    );
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(OnlineRepairService.this, "数据库更新错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(OnlineRepairService.this, "Please select a row to mark as completed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        tableButtonPanel.add(markCompletedBtn);

        // 创建底部面板并添加表格和按钮
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(tableButtonPanel, BorderLayout.SOUTH);
        bottomPanel.add(tableScrollPane, BorderLayout.CENTER);

        // 添加表格面板到底部区域
        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        // 添加动作监听器到提交按钮
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String community = communityField.getText();
                String owner = ownerField.getText();
                String type = (String)typeComboBox.getSelectedItem();
                String description = descriptionArea.getText();

                System.out.println("Submitted data:");
                System.out.println("Community: " + community);
                System.out.println("Owner: " + owner);
                System.out.println("Repair Type: " + type);
                System.out.println("Description: " + description);

                if (!community.isEmpty() && !owner.isEmpty() && !description.isEmpty() && !type.equals("请选择")) {
                    try {
                        // 插入数据库
                        mySql.use().insert(
                            Entity.create("onlinerepair_service")
                                .set("community", community)
                                .set("owner", owner)
                                .set("type", type)
                                .set("description", description)
                                .set("status", "Pending")
                        );
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(OnlineRepairService.this, "数据库插入错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }

                    // 更新表格
                    tableModel.addRow(new Object[]{
                        tableModel.getRowCount() + 1,
                        community,
                        owner,
                        type,
                        description,
                        "Pending"
                    });

                    // 清空表单
                    communityField.setText("");
                    ownerField.setText("");
                    typeComboBox.setSelectedIndex(0);
                    descriptionArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(OnlineRepairService.this, "Please fill all fields and select a repair type.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 设置主面板到窗口
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OnlineRepairService onlineRepairService = new OnlineRepairService();
            onlineRepairService.setVisible(true);
        });
    }
}