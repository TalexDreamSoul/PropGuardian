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
    private JTextField districtIdField;
    private JTextField buildingIdField;
    private JTextField roomIdField;
    private JTextField dateField; // 新增日期字段
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

        // 行 1：区域ID + 建筑ID
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("区域ID:"), gbc);
        gbc.gridx = 1;
        districtIdField = new JTextField();
        inputPanel.add(districtIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("建筑ID:"), gbc);
        gbc.gridx = 3;
        buildingIdField = new JTextField();
        inputPanel.add(buildingIdField, gbc);

        // 行 2：房间ID + 日期
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("房间ID:"), gbc);
        gbc.gridx = 1;
        roomIdField = new JTextField();
        inputPanel.add(roomIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("日期:"), gbc); // 新增日期标签
        gbc.gridx = 3;
        dateField = new JTextField(); // 新增日期文本框
        inputPanel.add(dateField, gbc);

        // 行 3：按钮
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        JButton queryButton = new JButton("查询总费用");
        inputPanel.add(queryButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // 表格初始化
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 查询按钮事件
        queryButton.addActionListener((ActionEvent e) -> {
            String districtId = districtIdField.getText().trim();
            String buildingId = buildingIdField.getText().trim();
            String roomId = roomIdField.getText().trim();
            String date = dateField.getText().trim(); // 获取日期值

            if (districtId.isEmpty() && buildingId.isEmpty() && roomId.isEmpty() && date.isEmpty()) {
                JOptionPane.showMessageDialog(UserBillingReport.this,
                        "请至少输入一个查询条件", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loadTotalFeesData(districtId, buildingId, roomId, date); // 添加日期参数
        });

        setVisible(true);
    }

    private void loadTotalFeesData(String districtId, String buildingId, String roomId, String date) {
        String[] columnNames = {"区域ID", "建筑ID", "房间ID", "日期", "总费用"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            StringBuilder sqlBuilder = new StringBuilder(
                "SELECT district_id, building_id, room_id, input_date AS date, " + 
                "(electric_reading + water_reading + gas_reading) AS total_fees " +
                "FROM meter_reading WHERE 1=1"
            );

            List<Object> params = new ArrayList<>();

            if (!districtId.isEmpty()) {
                sqlBuilder.append(" AND district_id = ?");
                params.add(Integer.parseInt(districtId));
            }
            if (!buildingId.isEmpty()) {
                sqlBuilder.append(" AND building_id = ?");
                params.add(Integer.parseInt(buildingId));
            }
            if (!roomId.isEmpty()) {
                sqlBuilder.append(" AND room_id = ?");
                params.add(Integer.parseInt(roomId));
            }
            if (!date.isEmpty()) {
                sqlBuilder.append(" AND input_date LIKE ?");
                params.add(date + "%");
            }

            List<Entity> feesList = this.db.query(sqlBuilder.toString(), params.toArray());

            for (Entity entity : feesList) {
                model.addRow(new Object[]{
                    entity.getInt("district_id"),
                    entity.getInt("building_id"),
                    entity.getInt("room_id"),
                    entity.getStr("date"),
                    entity.getDouble("total_fees")
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