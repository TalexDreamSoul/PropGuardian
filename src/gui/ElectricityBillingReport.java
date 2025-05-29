package gui;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ElectricityBillingReport extends JFrame {

    private JTable table;
    private JTextField roomIdField; // 修改变量名以匹配实际使用
    private JTextField dateField;   // 修改变量名以匹配实际使用
    private Db db; // 使用你封装的 db 实例


    public ElectricityBillingReport() {
        this.db = PropCore.INS.getMySql().use();

        setTitle("Electricity Billing Report");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // 行 2：房间ID + 日期
        gbc.gridx = 0; gbc.gridy = 0; // 修改行号为 0
        inputPanel.add(new JLabel("房间ID:"), gbc);
        gbc.gridx = 1;
        roomIdField = new JTextField(); // 初始化并赋值给类成员变量
        inputPanel.add(roomIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("日期:"), gbc);
        gbc.gridx = 3;
        dateField = new JTextField(); // 初始化并赋值给类成员变量
        inputPanel.add(dateField, gbc);

        // 行 3：按钮
        gbc.gridx = 0; gbc.gridy = 1; // 修改行号为 1
        gbc.gridwidth = 4;
        JButton queryButton = new JButton("查询");
        inputPanel.add(queryButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // 表格初始化
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 查询按钮事件
        queryButton.addActionListener((ActionEvent e) -> {
            String roomId = roomIdField.getText().trim();
            String date = dateField.getText().trim();

            if (roomId.isEmpty() && date.isEmpty()) {
                JOptionPane.showMessageDialog(ElectricityBillingReport.this,
                        "请至少输入一个查询条件", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loadFeesData(roomId, date);
        });

        setVisible(true);
        // 默认加载所有数据
        this.loadFeesData("", "");
    }

    private void loadFeesData(String roomId, String date) {
        // 更新列名以包含水表和燃气读数
        String[] columnNames = {"房间ID", "日期", "水表读数", "电费读数", "燃气读数"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            StringBuilder sqlBuilder = new StringBuilder(
                    "SELECT room_number, input_date AS date, " +
                            "water_reading, electric_reading, gas_reading " +  // 添加 water_reading 和 gas_reading
                            "FROM meter_reading WHERE 1=1"
            );

            List<Object> params = new ArrayList<>();

            if (!roomId.isEmpty()) {
                sqlBuilder.append(" AND room_number = ?");  // 修改为 room_number
                params.add(roomId);
            }
            if (!date.isEmpty()) {
                sqlBuilder.append(" AND input_date LIKE ?");
                params.add(date + "%");
            }

            List<Entity> feesList = this.db.query(sqlBuilder.toString(), params.toArray());

            for (Entity entity : feesList) {
                model.addRow(new Object[]{
                        entity.getStr("room_number"),  // 修改为 room_number
                        entity.getStr("date"),
                        entity.getDouble("water_reading"),  // 添加 water_reading
                        entity.getDouble("electric_reading"),
                        entity.getDouble("gas_reading")     // 添加 gas_reading
                });
            }

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "查询失败: " + ex.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }

        table.setModel(model);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ElectricityBillingReport());
    }
}

