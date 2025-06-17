package gui;

import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import dao.entity.UserInfo;
import utils.MentionUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserInfoPage extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, paswrdField, purviewField;

    public UserInfoPage() {
        setTitle("用户信息管理");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "姓名", "密码", "权限"};
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
            List<UserInfo> allData = userInfo.loadAllByEntity(uname, UserInfo.class);
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

            boolean storage = new UserInfo(-1, name, paswrd, purview).storage();
            MentionUtil.mentionForAdd(storage, this, this::refreshTable);
        });

        modifyBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要修改的行！");
                return;
            }

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

            UserInfo userInfo = new UserInfo(Integer.parseInt(String.valueOf(table.getValueAt(selectedRow, 0))), name, paswrd, purview);
            try {
                userInfo.getMySql().use().update(userInfo.getEntity().set("uname", userInfo.getUname())
                        .set("paswrd", userInfo.getPaswrd())
                        .set("purview", userInfo.getPurview()),
                        userInfo.getEntity().set("id", userInfo.getId())
                );
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            this.refreshTable();
        });

        deleteBtn.addActionListener(e -> {
            new UserInfo().deleteFixedEntity(new UserInfo().getEntity().set("uname", table.getValueAt(table.getSelectedRow(), 0)), this::refreshTable, this);
        });

        resetBtn.addActionListener(e -> {
            nameField.setText("");
            paswrdField.setText("");
            purviewField.setText("");
        });

        // 默认加载所有数据到表格中
        refreshTable();

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();

                nameField.setText(String.valueOf(table.getValueAt(row, 1)));
                paswrdField.setText(String.valueOf(table.getValueAt(row, 2)));
                purviewField.setText(String.valueOf(table.getValueAt(row, 3)));
            }
        });
    }

    private void refreshTable() {
        updateTable(new UserInfo().loadAllByEntity(UserInfo.class));
    }

    private void updateTable(List<UserInfo> results) {
        tableModel.setRowCount(0);

        if (results != null) {
            for (UserInfo info : results) {
                tableModel.addRow(new Object[] {
                        info.getId(),
                    info.getUname(),
                    info.getPaswrd(), info.getPurview()
                });
            }
        }
    }
}