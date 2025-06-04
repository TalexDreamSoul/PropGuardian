// PropertyBillingReport.java
// PropertyBillingReport.java
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;

public class PropertyBillingReport extends JFrame {

    private JTable table;
    private JTextField propertyIdField;
    private JTextField ownerIdField;
    private JTextField dateField;
    private Db db;
    private DefaultTableModel model; // 用于操作表格数据

    public PropertyBillingReport() {
        this.db = PropCore.INS.getMySql().use();

        setTitle("Property Billing Report");
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

        // 行 1：物业ID + 业主ID
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("物业ID:"), gbc);
        gbc.gridx = 1;
        propertyIdField = new JTextField();
        inputPanel.add(propertyIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("业主ID:"), gbc);
        gbc.gridx = 3;
        ownerIdField = new JTextField();
        inputPanel.add(ownerIdField, gbc);

        // 行 2：日期
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("日期:"), gbc);
        gbc.gridx = 1;
        dateField = new JTextField();
        inputPanel.add(dateField, gbc);

        // 行 3：按钮
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        JButton queryButton = new JButton("查询账单");
        inputPanel.add(queryButton, gbc);

        // 行 4：新增按钮
        gbc.gridy = 3;
        JButton addButton = new JButton("新增账单");
        inputPanel.add(addButton, gbc);

        // 行 5：删除按钮
        gbc.gridy = 4;
        JButton deleteButton = new JButton("删除账单");
        inputPanel.add(deleteButton, gbc);

        // 行 6：编辑按钮
        gbc.gridy = 5;
        JButton editButton = new JButton("编辑账单");
        inputPanel.add(editButton, gbc);

        // 行 7：查看全部按钮
        gbc.gridy = 6;
        JButton viewAllButton = new JButton("查看全部");
        inputPanel.add(viewAllButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // 表格初始化
        String[] columnNames = {"物业ID", "业主ID", "日期", "金额"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 查询按钮事件
        queryButton.addActionListener((ActionEvent e) -> {
            String propertyId = propertyIdField.getText().trim();
            String ownerId = ownerIdField.getText().trim();
            String date = dateField.getText().trim();

            if (propertyId.isEmpty() && ownerId.isEmpty() && date.isEmpty()) {
                JOptionPane.showMessageDialog(PropertyBillingReport.this,
                        "请至少输入一个查询条件", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loadBillingData(propertyId, ownerId, date);
        });

        // 查看全部按钮事件
        viewAllButton.addActionListener(e -> {
            propertyIdField.setText(""); // 清空输入框
            ownerIdField.setText("");
            dateField.setText("");
            loadBillingData("", "", ""); // 加载全部数据
        });

        // 新增按钮事件
        addButton.addActionListener(e -> {
            try {
                int propertyId = Integer.parseInt(propertyIdField.getText().trim());
                int ownerId = Integer.parseInt(ownerIdField.getText().trim());
                String billingDate = dateField.getText().trim();
                String amountStr = JOptionPane.showInputDialog(this, "请输入金额:");
                double amount = Double.parseDouble(amountStr);

                this.db.insert(
                        Entity.create("property_billing")
                                .set("property_id", propertyId)
                                .set("owner_id", ownerId)
                                .set("billing_date", billingDate)
                                .set("amount", amount)
                );
                JOptionPane.showMessageDialog(this, "新增成功！");
                loadBillingData("", "", "");
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "新增失败: " + ex.getMessage(),
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // 删除按钮事件
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请先选择一行数据！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int propertyId = (int) model.getValueAt(row, 0);
            int ownerId = (int) model.getValueAt(row, 1);
            String date = (String) model.getValueAt(row, 2);

            try {
                this.db.del(
                        Entity.create("property_billing")
                                .set("property_id", propertyId)
                                .set("owner_id", ownerId)
                                .set("billing_date", date)
                );
                JOptionPane.showMessageDialog(this, "删除成功！");
                loadBillingData("", "", "");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "删除失败: " + ex.getMessage(),
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // 编辑按钮事件
        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请先选择一行数据！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int propertyId = (int) model.getValueAt(row, 0);
            int ownerId = (int) model.getValueAt(row, 1);
            String oldDate = (String) model.getValueAt(row, 2);
            double oldAmount = (double) model.getValueAt(row, 3);

            String newDate = JOptionPane.showInputDialog(this, "请输入新的日期（YYYY-MM-DD）:", oldDate);
            String amountStr = JOptionPane.showInputDialog(this, "请输入新的金额:", oldAmount);
            double newAmount = Double.parseDouble(amountStr);

            if (newDate != null && !newDate.isEmpty()) {
                try {
                    this.db.update(
                            Entity.create("property_billing").set("billing_date", newDate).set("amount", newAmount),
                            Entity.create("property_billing")
                                    .set("property_id", propertyId)
                                    .set("owner_id", ownerId)
                                    .set("billing_date", oldDate)
                    );
                    JOptionPane.showMessageDialog(this, "更新成功！");
                    loadBillingData("", "", "");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "更新失败: " + ex.getMessage(),
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 页面初始化后加载全部数据
        loadBillingData("", "", "");

        setVisible(true);
    }

    private void loadBillingData(String propertyId, String ownerId, String date) {
        model.setRowCount(0); // 清空表格

        try {
            StringBuilder sqlBuilder = new StringBuilder(
                    "SELECT property_id, owner_id, billing_date AS date, amount " +
                            "FROM property_billing WHERE 1=1"
            );

            List<Object> params = new ArrayList<>();

            if (!propertyId.isEmpty()) {
                sqlBuilder.append(" AND property_id = ?");
                params.add(Integer.parseInt(propertyId));
            }
            if (!ownerId.isEmpty()) {
                sqlBuilder.append(" AND owner_id = ?");
                params.add(Integer.parseInt(ownerId));
            }
            if (!date.isEmpty()) {
                sqlBuilder.append(" AND billing_date LIKE ?");
                params.add(date + "%");
            }

            List<Entity> billingList = this.db.query(sqlBuilder.toString(), params.toArray());

            for (Entity entity : billingList) {
                model.addRow(new Object[]{
                        entity.getInt("property_id"),
                        entity.getInt("owner_id"),
                        entity.getStr("date"),
                        entity.getDouble("amount")
                });
            }

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "查询失败: " + ex.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PropertyBillingReport());
    }
}
