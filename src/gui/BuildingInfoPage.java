package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuildingInfoPage extends JFrame {

    private JTable table;
    private JTextField textFieldDistrictId, textFieldBuildingId, textFieldTotalStorey, textFieldTotalArea, textFieldHeight, textFieldType, textFieldStatus;
    private JButton queryButton, addButton, editButton, deleteButton, resetButton, updateButton;
    private JLabel loadingLabel;

    public BuildingInfoPage() {
        setTitle("楼宇信息维护");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));  // 设置间距

        // 顶部 Loading 提示
        loadingLabel = new JLabel("加载中...");
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        add(loadingLabel, BorderLayout.NORTH);

        // 初始化表格区域
        loadTableFromDatabase();

        // ---------- 表单输入区域 ----------
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("楼宇信息"));

        formPanel.add(new JLabel("小区编号:"));
        textFieldDistrictId = new JTextField();
        formPanel.add(textFieldDistrictId);

        formPanel.add(new JLabel("楼宇编号:"));
        textFieldBuildingId = new JTextField();
        formPanel.add(textFieldBuildingId);

        formPanel.add(new JLabel("楼宇层数:"));
        textFieldTotalStorey = new JTextField();
        formPanel.add(textFieldTotalStorey);

        formPanel.add(new JLabel("产权面积:"));
        textFieldTotalArea = new JTextField();
        formPanel.add(textFieldTotalArea);

        formPanel.add(new JLabel("楼宇高度:"));
        textFieldHeight = new JTextField();
        formPanel.add(textFieldHeight);

        formPanel.add(new JLabel("类型:"));
        textFieldType = new JTextField();
        formPanel.add(textFieldType);

        formPanel.add(new JLabel("楼宇状态:"));
        textFieldStatus = new JTextField();
        formPanel.add(textFieldStatus);

        // ---------- 按钮区域 ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        queryButton = new JButton("按小区查询");
        addButton = new JButton("添加");
        editButton = new JButton("修改");
        deleteButton = new JButton("删除");
        resetButton = new JButton("重置");
        updateButton = new JButton("更新");

        // 添加按钮监听
        setupButtonListeners();

        buttonPanel.add(queryButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(updateButton);

        // 整合底部表单 + 按钮
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadTableFromDatabase() {
        // 模拟数据
        String[] columnNames = {"小区名称", "楼宇编号", "楼宇层数", "产权面积", "楼宇高度", "类型"};
        Object[][] data = {
                {"小区A", "1", "5", "1000", "30", "住宅"},
                {"小区B", "2", "10", "2000", "50", "商业"}
        };

        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 加载完毕，隐藏 loading
        loadingLabel.setVisible(false);
    }

    private void setupButtonListeners() {
        queryButton.addActionListener(e -> queryBuilding());
        addButton.addActionListener(e -> addBuilding());
        editButton.addActionListener(e -> editBuilding());
        deleteButton.addActionListener(e -> deleteBuilding());
        resetButton.addActionListener(e -> resetFields());
        updateButton.addActionListener(e -> updateBuilding());
    }

    private void queryBuilding() {
        // TODO: 实现按小区查询逻辑
        JOptionPane.showMessageDialog(this, "查询功能待实现");
    }

    private void addBuilding() {
        // TODO: 实现添加逻辑
        JOptionPane.showMessageDialog(this, "添加功能待实现");
    }

    private void editBuilding() {
        // TODO: 实现编辑逻辑
        JOptionPane.showMessageDialog(this, "修改功能待实现");
    }

    private void deleteBuilding() {
        // TODO: 实现删除逻辑
        JOptionPane.showMessageDialog(this, "删除功能待实现");
    }

    private void updateBuilding() {
        // TODO: 实现更新逻辑
        JOptionPane.showMessageDialog(this, "更新功能待实现");
    }

    private void resetFields() {
        textFieldDistrictId.setText("");
        textFieldBuildingId.setText("");
        textFieldTotalStorey.setText("");
        textFieldTotalArea.setText("");
        textFieldHeight.setText("");
        textFieldType.setText("");
        textFieldStatus.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BuildingInfoPage());
    }
}
