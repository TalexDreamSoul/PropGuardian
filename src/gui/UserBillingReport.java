package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import cn.hutool.db.Db; // 添加缺失的导入语句
import cn.hutool.db.Entity;
import core.PropCore; // 添加 PropCore 的导入语句

public class UserBillingReport extends JFrame {

    private JTable table;
    private JTextField roomIdField; // 修改为仅保留房间ID字段
    private JTextField dateField;   // 保留日期字段
    private Db db;

    public UserBillingReport() {
        this.db = PropCore.INS.getMySql().use();

        setTitle("User Billing Report");
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

        // 行 1：房间ID + 日期
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("房间ID:"), gbc);
        gbc.gridx = 1;
        roomIdField = new JTextField();
        inputPanel.add(roomIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("日期:"), gbc);
        gbc.gridx = 3;
        dateField = new JTextField();
        inputPanel.add(dateField, gbc);

        // 行 2：查询按钮
        gbc.gridx = 0; gbc.gridy = 1;
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
                JOptionPane.showMessageDialog(UserBillingReport.this,
                        "请至少输入一个查询条件", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loadFeesData(roomId, date); // 改为调用loadFeesData
        });

        setVisible(true);
        this.loadFeesData("", ""); // 默认加载所有数据
    }

    // 修改查询方法名称和逻辑
    private void loadFeesData(String roomId, String date) {
        // 更新列名以包含各单项费用
        String[] columnNames = {"房间ID", "日期", "水表读数", "电费读数", "燃气读数"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            StringBuilder sqlBuilder = new StringBuilder(
                    "SELECT room_number, input_date AS date, " +  // 将 room_id 改为 room_number
                            "water_reading, electric_reading, gas_reading " +
                            "FROM meter_reading WHERE 1=1"
            );

            List<Object> params = new ArrayList<>();

            if (!roomId.isEmpty()) {
                sqlBuilder.append(" AND room_number = ?");  // 将 room_id 改为 room_number
                params.add(roomId);  // 移除 Integer.parseInt，直接使用 roomId
            }
            if (!date.isEmpty()) {
                sqlBuilder.append(" AND input_date LIKE ?");
                params.add(date + "%");
            }

            List<Entity> feesList = this.db.query(sqlBuilder.toString(), params.toArray());

            for (Entity entity : feesList) {
                model.addRow(new Object[]{
                        entity.getStr("room_number"),  // 将 getInt("room_id") 改为 getStr("room_number")
                        entity.getStr("date"),
                        entity.getDouble("water_reading"),
                        entity.getDouble("electric_reading"),
                        entity.getDouble("gas_reading")
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
        SwingUtilities.invokeLater(() -> new UserBillingReport());
    }
}