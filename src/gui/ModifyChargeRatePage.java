package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
public class ModifyChargeRatePage extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ModifyChargeRatePage() {
        setTitle("Modify Charge Rate");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建表格部分
        String[] columnNames = {"收费项目", "单位", "单价（元）"};
        Object[][] data = {
                {"水费", "元/吨", 3.0},
                {"电费", "元/度", 0.5},
                {"燃气费", "元/立方米", 2.5}
        };

        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("增加");
        JButton editButton = new JButton("修改");
        JButton deleteButton = new JButton("删除");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // 为“增加”按钮添加功能
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String item = JOptionPane.showInputDialog("请输入收费项目");
                String unit = JOptionPane.showInputDialog("请输入单位");
                String priceStr = JOptionPane.showInputDialog("请输入单价（元）");
                if (item != null && unit != null && priceStr != null) {
                    try {
                        double price = Double.parseDouble(priceStr);
                        tableModel.addRow(new Object[]{item, unit, price});
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "单价输入有误，请输入数字！");
                    }
                }
            }
        });

        // 为“修改”按钮添加功能
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String item = JOptionPane.showInputDialog("请输入新的收费项目", table.getValueAt(selectedRow, 0));
                    String unit = JOptionPane.showInputDialog("请输入新的单位", table.getValueAt(selectedRow, 1));
                    String priceStr = JOptionPane.showInputDialog("请输入新的单价（元）", table.getValueAt(selectedRow, 2));
                    if (item != null && unit != null && priceStr != null) {
                        try {
                            double price = Double.parseDouble(priceStr);
                            tableModel.setValueAt(item, selectedRow, 0);
                            tableModel.setValueAt(unit, selectedRow, 1);
                            tableModel.setValueAt(price, selectedRow, 2);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "单价输入有误，请输入数字！");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请先选择要修改的行！");
                }
            }
        });

        // 为“删除”按钮添加功能
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int option = JOptionPane.showConfirmDialog(null, "确定要删除该行吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请先选择要删除的行！");
                }
            }
        });

        // 将组件添加到主面板
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 设置内容面板
        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ModifyChargeRatePage page = new ModifyChargeRatePage();
            page.setVisible(true);
        });
    }
}
