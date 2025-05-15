package gui;

import db.MySql;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

public class BuildingInfoPage extends JFrame {

    private static final Logger logger = Logger.getLogger(BuildingInfoPage.class.getName());

    private JTable table;
    private JTextField textFieldDistrictId, textFieldBuildingId, textFieldTotalStorey, textFieldTotalArea, textFieldHeight, textFieldType, textFieldStatus;
    private JButton queryButton, addButton, editButton, deleteButton, resetButton, updateButton;
    private JLabel loadingLabel;
    private MySql mySql;

    public BuildingInfoPage() {
        // 初始化 MySql 实例
        this.mySql = new MySql();  // 直接初始化 MySql 实例

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
        String[] columnNames = {"小区名称", "楼宇编号", "楼宇层数", "产权面积", "楼宇高度", "类型"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {
            Connection connection = mySql.getSimpleDataSource().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM building_info");

            while (resultSet.next()) {
                Object[] row = new Object[table.getColumnModel().getColumnCount()];
                row[0] = resultSet.getString("communityName");
                row[1] = resultSet.getString("buildingId");
                row[2] = resultSet.getInt("totalStorey");
                row[3] = resultSet.getDouble("totalArea");
                row[4] = resultSet.getDouble("height");
                row[5] = resultSet.getString("type");
                tableModel.addRow(row);
            }

            // 加载完毕，隐藏 loading
            loadingLabel.setVisible(false);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "数据库连接失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error connecting to MySQL: " + e.getMessage());
        }

        this.table = new JTable();  // 初始化 table 防止 NullPointerException
        table.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
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
