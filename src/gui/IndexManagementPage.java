package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;

public class IndexManagementPage extends JFrame {
    private Db db;
    private JTable table;

    // 确保 IndexManagementPage 构造函数能够接收 community 和 building 参数
    public IndexManagementPage(String community, String building) {
        setTitle("业主水/电/气指数管理");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        db = PropCore.INS.getMySql().use();
        setLayout(new BorderLayout());

        // 创建表格模型
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("日期");
        tableModel.addColumn("房间号");
        tableModel.addColumn("水表读数");
        tableModel.addColumn("电表读数");
        tableModel.addColumn("气表读数");

        // 从数据库加载数据
        loadDataFromDatabase(tableModel, community, building);

        // 创建表格并设置模型
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 添加按钮区域
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = new JButton("删除");
        JButton updateButton = new JButton("修改");
        JButton backButton = new JButton("返回"); // 新增返回按钮

        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton); // 添加返回按钮到面板
        add(buttonPanel, BorderLayout.SOUTH);

        // 删除按钮事件监听器
        deleteButton.addActionListener(e -> handleDelete());

        // 修改按钮事件监听器
        updateButton.addActionListener(e -> handleUpdate());

        // 返回按钮事件监听器
        backButton.addActionListener(e -> {
            dispose(); // 关闭当前页面
        });

        setVisible(true);
    }

    // 从数据库加载数据
    private void loadDataFromDatabase(DefaultTableModel tableModel, String community, String building) {
        try {
            // 验证 community 和 building 是否为有效的数字字符串
            if (community == null || community.isEmpty() || !community.matches("\\d+")) {
                throw new IllegalArgumentException("社区ID必须为有效的数字字符串");
            }
            if (building == null || building.isEmpty() || !building.matches("\\d+")) {
                throw new IllegalArgumentException("楼宇ID必须为有效的数字字符串");
            }

            List<Entity> results = db.query("SELECT input_date, room_number, water_reading, electric_reading, gas_reading FROM meter_reading WHERE district_id = ? AND building_id = ?", Integer.parseInt(community), Integer.parseInt(building));
            if (results.isEmpty()) {
                System.out.println("数据库中没有找到符合条件的数据");
            }
            for (Entity entity : results) {
                tableModel.addRow(new Object[]{
                        entity.getStr("input_date"),
                        entity.getStr("room_number"),
                        entity.getDouble("water_reading"),
                        entity.getDouble("electric_reading"),
                        entity.getDouble("gas_reading")
                });
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "参数错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // 删除记录
    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String date = (String) table.getValueAt(selectedRow, 0);
        String roomNumber = (String) table.getValueAt(selectedRow, 1);

        try {
            db.del(Entity.create("meter_reading").set("input_date", date).set("room_number", roomNumber));
            ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "记录已删除", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "删除失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // 修改记录
    private void handleUpdate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要修改的记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String date = (String) table.getValueAt(selectedRow, 0);
        String roomNumber = (String) table.getValueAt(selectedRow, 1);
        double waterReading = (double) table.getValueAt(selectedRow, 2);
        double electricReading = (double) table.getValueAt(selectedRow, 3);
        double gasReading = (double) table.getValueAt(selectedRow, 4);

        // 弹出对话框让用户输入新的值
        String newWaterReading = JOptionPane.showInputDialog(this, "请输入新的水表读数", String.valueOf(waterReading));
        String newElectricReading = JOptionPane.showInputDialog(this, "请输入新的电表读数", String.valueOf(electricReading));
        String newGasReading = JOptionPane.showInputDialog(this, "请输入新的气表读数", String.valueOf(gasReading));

        try {
            db.update(Entity.create("meter_reading")
                            .set("water_reading", Double.parseDouble(newWaterReading))
                            .set("electric_reading", Double.parseDouble(newElectricReading))
                            .set("gas_reading", Double.parseDouble(newGasReading)),
                    Entity.create("meter_reading")
                            .set("input_date", date)
                            .set("room_number", roomNumber));

            // 更新表格中的数据
            table.setValueAt(Double.parseDouble(newWaterReading), selectedRow, 2);
            table.setValueAt(Double.parseDouble(newElectricReading), selectedRow, 3);
            table.setValueAt(Double.parseDouble(newGasReading), selectedRow, 4);

            JOptionPane.showMessageDialog(this, "记录已更新", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "更新失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 使用示例参数启动页面
                String community = "1"; // 示例社区ID
                String building = "1"; // 示例楼宇ID
                new IndexManagementPage(community, building);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "程序启动失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}