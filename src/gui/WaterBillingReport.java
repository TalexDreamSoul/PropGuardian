package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.db.Entity;
import cn.hutool.db.Db;
import core.PropCore;

public class WaterBillingReport extends JFrame {

    private JTable table;
    private JTextField ownerIdField;
    private Db db; // 使用你封装的 db 实例

    public WaterBillingReport() {
        this.db = PropCore.INS.getMySql().use();

        setTitle("Water Billing Report");
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
        JTextField districtIdField = new JTextField();
        inputPanel.add(districtIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("建筑ID:"), gbc);
        gbc.gridx = 3;
        JTextField buildingIdField = new JTextField();
        inputPanel.add(buildingIdField, gbc);

        // 行 2：房间ID + 日期
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("房间ID:"), gbc);
        gbc.gridx = 1;
        JTextField roomIdField = new JTextField();
        inputPanel.add(roomIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("日期:"), gbc);
        gbc.gridx = 3;
        JTextField dateField = new JTextField();
        inputPanel.add(dateField, gbc);

        // 行 3：按钮
        gbc.gridx = 0; gbc.gridy = 2;
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
            String districtId = districtIdField.getText().trim();
            String buildingId = buildingIdField.getText().trim();
            String roomId = roomIdField.getText().trim();
            String date = dateField.getText().trim();

            if (districtId.isEmpty() && buildingId.isEmpty() &&
                    roomId.isEmpty() && date.isEmpty()) {
                JOptionPane.showMessageDialog(WaterBillingReport.this,
                        "请至少输入一个查询条件", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loadFeesData(districtId, buildingId, roomId, date);
        });

        setVisible(true);
    }

    private void loadFeesData(String districtId, String buildingId, String roomId, String date) {
        String[] columnNames = {"区域ID", "建筑ID", "房间ID", "日期", "水费读数"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            StringBuilder sqlBuilder = new StringBuilder(
                    "SELECT district_id, building_id, room_id, date, water_reading " +
                            "FROM master_use WHERE 1=1"
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
                sqlBuilder.append(" AND date LIKE ?");
                params.add(date + "%");
            }

            List<Entity> feesList = this.db.query(sqlBuilder.toString(), params.toArray());

            for (Entity entity : feesList) {
                model.addRow(new Object[]{
                        entity.getInt("district_id"),
                        entity.getInt("building_id"),
                        entity.getInt("room_id"),
                        entity.getStr("date"),
                        entity.getDouble("water_reading")
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
        SwingUtilities.invokeLater(WaterBillingReport::new);
    }
}
