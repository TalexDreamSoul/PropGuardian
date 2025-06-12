package gui;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;
import dao.entity.UserInfo;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserInfoPage extends JFrame {
    private JTable table;
    private Db db;
    private DefaultTableModel tableModel;
    private JTextField nameField, paswrdField, purviewField;

    public UserInfoPage() {
        setTitle("用户信息管理");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        this.db = PropCore.INS.getMySql().use();

        String[] columnNames = {"姓名", "密码", "权限"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        nameField = new JTextField(10);
        paswrdField = new JTextField(10);
        purviewField = new JTextField(10);

        JButton queryByNameBtn = new JButton("姓名查询");
        JButton addBtn = new JButton("添加");
        JButton modifyBtn = new JButton("修改");
        JButton deleteBtn = new JButton("删除");
        JButton resetBtn = new JButton("重置");

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("姓名")); inputPanel.add(nameField);
        inputPanel.add(new JLabel("密码")); inputPanel.add(paswrdField);
        inputPanel.add(new JLabel("权限")); inputPanel.add(purviewField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(queryByNameBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(modifyBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(resetBtn);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        queryByNameBtn.addActionListener(e -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入姓名进行查询！");
                return;
            }

            UserInfo userInfo = new UserInfo();
            Entity uname = userInfo.getQueryEntity("uname", name);
            List<UserInfo> allData = userInfo.loadAll(uname).stream().map(entity -> new UserInfo(entity.getStr("uname"), entity.getStr("paswrd"), entity.getInt("purview"))).toList();
            updateTable(allData);
        });

        addBtn.addActionListener(e -> {
            String name = nameField.getText();
            String paswrd = paswrdField.getText();
            String purviewStr = purviewField.getText();
            
            if (name.isEmpty() || paswrd.isEmpty() || purviewStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整信息！");
                return;
            }
            
            int purview;
            try {
                purview = Integer.parseInt(purviewStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "权限必须为数字！");
                return;
            }

            boolean storage = new UserInfo(name, paswrd, purview).storage();
            if (storage) {
                JOptionPane.showMessageDialog(this, "添加成功！");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "添加失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        modifyBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要修改的行！");
                return;
            }

            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText();
            String paswrd = paswrdField.getText();
            String purviewStr = purviewField.getText();
            
            if (name.isEmpty() || paswrd.isEmpty() || purviewStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整信息！");
                return;
            }
            
            int purview;
            try {
                purview = Integer.parseInt(purviewStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "权限必须为数字！");
                return;
            }

            String sql = "UPDATE userinfo SET uname=?, paswrd=?, purview=? WHERE id=?";
            try {
                int result = db.execute(sql, name, paswrd, purview, userId);

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "修改成功！");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "修改失败！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "修改失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要删除的行！");
                return;
            }
            boolean success = new UserInfo().delete("id", (String) tableModel.getValueAt(selectedRow, 0));
            if (success) {
                JOptionPane.showMessageDialog(this, "删除成功！");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        resetBtn.addActionListener(e -> {
            nameField.setText("");
            paswrdField.setText("");
            purviewField.setText("");
        });

        // 默认加载所有数据到表格中
        refreshTable();
    }

    private void refreshTable() {
        List<Entity> query = new UserInfo().loadAll();
        List<UserInfo> allData = query.stream().map(entity -> new UserInfo(entity.getStr("uname"), entity.getStr("paswrd"), entity.getInt("purview"))).toList();
        updateTable(allData);
    }

    private void updateTable(List<UserInfo> results) {
        tableModel.setRowCount(0);
        if (results != null) {
            for (UserInfo info : results) {
                tableModel.addRow(new Object[] {
                    info.getUname(),
                    info.getPaswrd(), info.getPurview()
                });
            }
        }
    }
}