package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;

public class ModifyChargeRatePage extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private Db db; // 使用 Hutool Db 工具

    public ModifyChargeRatePage() {
        db = PropCore.INS.getMySql().use(); // 初始化数据库连接

        setTitle("Modify Charge Rate");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建表格部分
        String[] columnNames = {"收费项目", "单位", "单价（元）"};
        tableModel = new DefaultTableModel(columnNames, 0); // 空模型
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // 加载数据库数据
        loadTableFromDatabase();

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("增加");
        JButton editButton = new JButton("修改");
        JButton deleteButton = new JButton("删除");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // 为“增加”按钮添加功能
        addButton.addActionListener(e -> addChargeRate());

        // 为“修改”按钮添加功能
        editButton.addActionListener(e -> editChargeRate());

        // 为“删除”按钮添加功能
        deleteButton.addActionListener(e -> deleteChargeRate());

        // 将组件添加到主面板
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    // 从数据库加载数据
    private void loadTableFromDatabase() {
        try {
            tableModel.setRowCount(0); // 清空现有数据

            List<Entity> list = db.query("SELECT * FROM modifycharge_ratepage"); // 修改表名
            for (Entity entity : list) {
                Object[] row = new Object[]{
                        entity.getStr("item"),
                        entity.getStr("unit"),
                        entity.getBigDecimal("price")
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载失败: " + ex.getMessage());
        }
    }


    // 新增收费项目
    private void addChargeRate() {
        String item = JOptionPane.showInputDialog("请输入收费项目");
        String unit = JOptionPane.showInputDialog("请输入单位");
        String priceStr = JOptionPane.showInputDialog("请输入单价（元）");

        if (item == null || unit == null || priceStr == null) return;

        try {
            double price = Double.parseDouble(priceStr);
            db.insert(Entity.create("modifycharge_ratepage") // 修改表名
                    .set("item", item)
                    .set("unit", unit)
                    .set("price", price));

            loadTableFromDatabase();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "单价输入有误，请输入数字！");
        } catch (Exception e) {
            JOptionPane. showMessageDialog(null, "新增失败: " + e.getMessage());
        }
    }

    // 修改收费项目
    private void editChargeRate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "请先选择要修改的行！");
            return;
        }

        String oldItem = (String) table.getValueAt(selectedRow, 0);
        String item = JOptionPane.showInputDialog("请输入新的收费项目", oldItem);
        String unit = JOptionPane.showInputDialog("请输入新的单位", table.getValueAt(selectedRow, 1));
        String priceStr = JOptionPane.showInputDialog("请输入新的单价（元）", table.getValueAt(selectedRow, 2));

        if (item == null || unit == null || priceStr == null) return;

        try {
            double price = Double.parseDouble(priceStr);
            db.update(
                    Entity.create("modifycharge_ratepage") // 修改表名
                            .set("item", item)
                            .set("unit", unit)
                            .set("price", price),
                    Entity.create().set("item", oldItem)
            );

            loadTableFromDatabase();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "单价输入有误，请输入数字！");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "修改失败: " + e.getMessage());
        }
    }

    // 删除收费项目
    private void deleteChargeRate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "请先选择要删除的行！");
            return;
        }

        String item = (String) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(null, "确定要删除该项目吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                db.del(Entity.create("modifycharge_ratepage").set("item", item)); // 修改表名

                loadTableFromDatabase();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "删除失败: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ModifyChargeRatePage page = new ModifyChargeRatePage();
            page.setVisible(true);
        });
    }
}
