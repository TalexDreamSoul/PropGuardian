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

public class GasBillingReport extends JFrame {

    private JTable table;
    private JTextField districtIdField;
    private JTextField buildingIdField;
    private JTextField roomIdField;
    private JTextField dateField;

    // 添加缺失的字段定义
    private boolean confirmed = false;
    private String selectedCommunityId = "";
    private String selectedBuildingId = "";

    private Db db;

    // 添加继承方法
    public boolean isConfirmed() {
        return confirmed;
    }

    public String getSelectedCommunity() {
        return selectedCommunityId;
    }

    public String getSelectedBuilding() {
        return selectedBuildingId;
    }

    public GasBillingReport() {
        this.db = PropCore.INS.getMySql().use();

        setTitle("Gas Billing Report");
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
        districtIdField.setEditable(false);
        inputPanel.add(districtIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("建筑ID:"), gbc);
        gbc.gridx = 3;
        buildingIdField = new JTextField();
        buildingIdField.setEditable(false);
        inputPanel.add(buildingIdField, gbc);

        // 新增选择按钮
        gbc.gridx = 4;
        gbc.gridy = 0;
        JButton selectButton = new JButton("选择小区/楼宇");
        inputPanel.add(selectButton, gbc);

        // 行 2：房间ID + 日期
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("房间ID:"), gbc);
        gbc.gridx = 1;
        roomIdField = new JTextField();
        inputPanel.add(roomIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("日期:"), gbc);
        gbc.gridx = 3;
        dateField = new JTextField();
        inputPanel.add(dateField, gbc);

        // 行 3：按钮
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 5;
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

            if ((districtId.isEmpty() && buildingId.isEmpty() &&
                    roomId.isEmpty() && date.isEmpty()) ||
                (districtId.equals("选择小区") || buildingId.equals("选择楼宇"))) {
                JOptionPane.showMessageDialog(GasBillingReport.this,
                        "请至少输入一个有效查询条件", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loadFeesData(districtId, buildingId, roomId, date);
        });

        // 选择按钮事件
        selectButton.addActionListener(e -> {
            SelectCommunityAndBuilding selector = new SelectCommunityAndBuilding();
            selector.setVisible(true);
            // 添加窗口关闭监听器获取选择结果
            selector.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    if (selector.isConfirmed()) {
                        selectedCommunityId = selector.getSelectedCommunity();
                        selectedBuildingId = selector.getSelectedBuilding();
                        districtIdField.setText(selectedCommunityId);
                        buildingIdField.setText(selectedBuildingId);
                    }
                }
            });
        });

        setVisible(true);
    }

    // 在GasBillingReport中添加重置方法
    private void resetForm() {
        districtIdField.setText("");
        buildingIdField.setText("");
        roomIdField.setText("");
        dateField.setText("");
        selectedCommunityId = "";
        selectedBuildingId = "";
        confirmed = false;
        table.setModel(new DefaultTableModel());
    }

    protected void onConfirm() {
        String community = getSelectedCommunity();
        String building = getSelectedBuilding();
        
        if ("选择小区".equals(community) || "选择楼宇".equals(building)) {
            JOptionPane.showMessageDialog(this, "请选择有效的小区和楼宇", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        new OwnerIndexEntryPage(community, building).setVisible(true);
        dispose();
    }

    private void loadFeesData(String districtId, String buildingId, String roomId, String date) {
        String[] columnNames = {"区域ID", "建筑ID", "房间ID", "日期", "燃气读数"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            StringBuilder sqlBuilder = new StringBuilder(
                "SELECT district_id, building_id, room_id, input_date AS date, gas_reading " +
                        "FROM meter_reading WHERE 1=1"
            );

            List<Object> params = new ArrayList<>();

            if (!districtId.isEmpty() && !districtId.equals("选择小区")) {
                sqlBuilder.append(" AND district_id = ?");
                params.add(Integer.parseInt(districtId));
            }
            if (!buildingId.isEmpty() && !buildingId.equals("选择楼宇")) {
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
        SwingUtilities.invokeLater(GasBillingReport::new);
    }
}
