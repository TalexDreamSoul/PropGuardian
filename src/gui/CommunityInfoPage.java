package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;

import java.util.List;

public class CommunityInfoPage extends JFrame {
    private JTable table;
    private JTextField textFieldId;
    private JTextField textFieldName;
    private JTextField textFieldAddress;
    private JTextField textFieldArea;
    private JLabel totalLabel; // 统计标签
    private JLabel loadingLabel; // 加载动画标签

    private Db db;

    public CommunityInfoPage() {
        Db use = PropCore.INS.getMySql().use();
        this.db = use;

        setTitle("小区信息维护");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // 设置布局
        setLayout(new BorderLayout());

        // 创建加载动画标签
        loadingLabel = new JLabel("加载中...");
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        add(loadingLabel, BorderLayout.CENTER);

        // 初始化表格
        loadTableFromDatabase();

        // 创建统计面板
        JPanel statsPanel = new JPanel();
        totalLabel = new JLabel("小区总数: 0, 总面积: 0.0 平方米");
//        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(totalLabel);
        add(statsPanel, BorderLayout.NORTH);

        // 创建输入框面板
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("小区编号:"));
        textFieldId = new JTextField();
        inputPanel.add(textFieldId);
        inputPanel.add(new JLabel("小区名称:"));
        textFieldName = new JTextField();
        inputPanel.add(textFieldName);
        inputPanel.add(new JLabel("小区地址:"));
        textFieldAddress = new JTextField();
        inputPanel.add(textFieldAddress);
        inputPanel.add(new JLabel("占地面积:"));
        textFieldArea = new JTextField();
        inputPanel.add(textFieldArea);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加");
        JButton editButton = new JButton("修改");
        JButton deleteButton = new JButton("删除");
        JButton resetButton = new JButton("重置");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCommunity();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCommunity();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCommunity();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(resetButton);

        // 将输入框和按钮面板添加到底部
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // 更新统计信息
        updateStatistics();
    }

    // 加载数据库数据到表格
    private void loadTableFromDatabase() {
        try {
            // 检查数据库是否为空，如果为空则插入默认数据
            if (db.query("SELECT COUNT(*) FROM community_info").get(0).getLong("COUNT(*)") == 0) {
                insertDefaultData();
            }

            // 显示加载动画
            loadingLabel.setVisible(true);

            List<Entity> communityList = this.db.query("SELECT * FROM community_info");
            Object[][] data = new Object[communityList.size()][4];
            for (int i = 0; i < communityList.size(); i++) {
                Entity community = communityList.get(i);
                data[i][0] = community.getLong("district_id");
                data[i][1] = community.getStr("district_name");
                data[i][2] = community.getStr("address");
                data[i][3] = community.getDouble("floor_space");
            }
            String[] columnNames = {"小区编号", "小区名称", "小区地址", "占地面积"};
            table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            // 隐藏加载动画
            loadingLabel.setVisible(false);

            // 添加 ListSelectionListener
            table.getSelectionModel().addListSelectionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    textFieldId.setText(table.getValueAt(selectedRow, 0).toString());
                    textFieldName.setText(table.getValueAt(selectedRow, 1).toString());
                    textFieldAddress.setText(table.getValueAt(selectedRow, 2).toString());
                    textFieldArea.setText(table.getValueAt(selectedRow, 3).toString());
                } else {
                    resetFields();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载数据失败: " + e.getMessage());
        }
    }

    // 插入默认数据
    private void insertDefaultData() {
        try {
            this.db.insert(Entity.create("community_info")
                    .set("district_id", 1L)
                    .set("district_name", "小区A")
                    .set("address", "地址A")
                    .set("floor_space", 1000.0));
            this.db.insert(Entity.create("community_info")
                    .set("district_id", 2L)
                    .set("district_name", "小区B")
                    .set("address", "地址B")
                    .set("floor_space", 1500.0));
            this.db.insert(Entity.create("community_info")
                    .set("district_id", 3L)
                    .set("district_name", "小区C")
                    .set("address", "地址C")
                    .set("floor_space", 2000.0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 添加小区信息
    private void addCommunity() {
        try {
            long id = Long.parseLong(textFieldId.getText());
            String name = textFieldName.getText();
            String address = textFieldAddress.getText();
            double area = Double.parseDouble(textFieldArea.getText());

            this.db.insert(Entity.create("community_info")
                    .set("district_id", id)
                    .set("district_name", name)
                    .set("address", address)
                    .set("floor_space", area));

            loadTableFromDatabase();
            updateStatistics();
            resetFields();
            JOptionPane.showMessageDialog(this, "添加成功！");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "添加失败: " + e.getMessage());
        }
    }

    // 修改小区信息
    private void editCommunity() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请先选择要修改的小区！");
                return;
            }

            long id = Long.parseLong(textFieldId.getText());
            String name = textFieldName.getText();
            String address = textFieldAddress.getText();
            double area = Double.parseDouble(textFieldArea.getText());

            this.db.update(Entity.create("community_info")
                            .set("district_name", name)
                            .set("address", address)
                            .set("floor_space", area),
                    Entity.create().set("district_id", id));

            loadTableFromDatabase();
            updateStatistics();
            resetFields();
            JOptionPane.showMessageDialog(this, "修改成功！");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "修改失败: " + e.getMessage());
        }
    }

    // 删除小区信息
    private void deleteCommunity() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请先选择要删除的小区！");
                return;
            }

            long id = Long.parseLong(table.getValueAt(selectedRow, 0).toString());
            this.db.del(Entity.create("community_info").set("district_id", id));

            loadTableFromDatabase();
            updateStatistics();
            resetFields();
            JOptionPane.showMessageDialog(this, "删除成功！");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "删除失败: " + e.getMessage());
        }
    }

    // 重置输入框
    private void resetFields() {
        textFieldId.setText("");
        textFieldName.setText("");
        textFieldAddress.setText("");
        textFieldArea.setText("");
    }

    // 更新统计信息
    private void updateStatistics() {
        try {
            int rowCount = table.getRowCount();
            double totalArea = 0.0;
            for (int i = 0; i < rowCount; i++) {
                totalArea += Double.parseDouble(table.getValueAt(i, 3).toString());
            }
            totalLabel.setText("小区总数: " + rowCount + ", 总面积: " + totalArea + " 平方米");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CommunityInfoPage page = new CommunityInfoPage();
            page.setVisible(true);
        });
    }
}