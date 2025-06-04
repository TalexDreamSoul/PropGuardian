package gui;

import cn.hutool.db.Db;
import core.PropCore;
import dao.entity.UserInfo;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserInfoPage extends JFrame {
    private JTable table;
    private Db db;
    private DefaultTableModel tableModel;
    private JTextField nameField, genderField, birthDateField, idCardField, contactPhoneField,
            householdAddressField, currentAddressField, moveInTimeField, relationshipField,
            workUnitField, remarkField;

    public UserInfoPage() {
        setTitle("用户信息管理");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        try {
            this.db = PropCore.INS.getMySql().use();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "数据库连接失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        String[] columnNames = {"姓名", "性别", "出生日期", "身份证号", "联系电话", "户籍地址", "现住地址", "入住时间", "与户主关系", "工作单位", "备注"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        nameField = new JTextField(10);
        genderField = new JTextField(10);
        birthDateField = new JTextField(10);
        idCardField = new JTextField(10);
        contactPhoneField = new JTextField(10);
        householdAddressField = new JTextField(10);
        currentAddressField = new JTextField(10);
        moveInTimeField = new JTextField(10);
        relationshipField = new JTextField(10);
        workUnitField = new JTextField(10);
        remarkField = new JTextField(10);

        JButton queryByNameBtn = new JButton("姓名查询");
        JButton addBtn = new JButton("添加");
        JButton modifyBtn = new JButton("修改");
        JButton deleteBtn = new JButton("删除");
        JButton resetBtn = new JButton("重置");

        JPanel inputPanel = new JPanel(new GridLayout(11, 2));
        inputPanel.add(new JLabel("姓名")); inputPanel.add(nameField);
        inputPanel.add(new JLabel("性别")); inputPanel.add(genderField);
        inputPanel.add(new JLabel("出生日期")); inputPanel.add(birthDateField);
        inputPanel.add(new JLabel("身份证号")); inputPanel.add(idCardField);
        inputPanel.add(new JLabel("联系电话")); inputPanel.add(contactPhoneField);
        inputPanel.add(new JLabel("户籍地址")); inputPanel.add(householdAddressField);
        inputPanel.add(new JLabel("现住地址")); inputPanel.add(currentAddressField);
        inputPanel.add(new JLabel("入住时间")); inputPanel.add(moveInTimeField);
        inputPanel.add(new JLabel("与户主关系")); inputPanel.add(relationshipField);
        inputPanel.add(new JLabel("工作单位")); inputPanel.add(workUnitField);
        inputPanel.add(new JLabel("备注")); inputPanel.add(remarkField);

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
            try {
                String sql = "SELECT * FROM user_info WHERE name = ?";
                List<UserInfo> results = db.query(sql, UserInfo.class, name);
                updateTable(results);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        addBtn.addActionListener(e -> {
            String name = nameField.getText();
            String gender = genderField.getText();
            String birthDate = birthDateField.getText();
            String idCard = idCardField.getText();
            String contactPhone = contactPhoneField.getText();
            String householdAddress = householdAddressField.getText();
            String currentAddress = currentAddressField.getText();
            String moveInTime = moveInTimeField.getText();
            String relationship = relationshipField.getText();
            String workUnit = workUnitField.getText();
            String remark = remarkField.getText();

            if (name.isEmpty() || gender.isEmpty() || birthDate.isEmpty() || idCard.isEmpty() || contactPhone.isEmpty()
                    || householdAddress.isEmpty() || currentAddress.isEmpty() || moveInTime.isEmpty() || relationship.isEmpty()
                    || workUnit.isEmpty() || remark.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整信息！");
                return;
            }

            String sql = "INSERT INTO user_info(name, gender, birth_date, id_card, contact_phone, household_address, " +
                    "current_address, move_in_time, relationship, work_unit, remark) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            try {
                int result = db.execute(sql, name, gender, birthDate, idCard, contactPhone, householdAddress,
                        currentAddress, moveInTime, relationship, workUnit, remark);

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "添加成功！");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "添加失败！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "添加失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
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
            String gender = genderField.getText();
            String birthDate = birthDateField.getText();
            String idCard = idCardField.getText();
            String contactPhone = contactPhoneField.getText();
            String householdAddress = householdAddressField.getText();
            String currentAddress = currentAddressField.getText();
            String moveInTime = moveInTimeField.getText();
            String relationship = relationshipField.getText();
            String workUnit = workUnitField.getText();
            String remark = remarkField.getText();

            if (name.isEmpty() || gender.isEmpty() || birthDate.isEmpty() || idCard.isEmpty() || contactPhone.isEmpty()
                    || householdAddress.isEmpty() || currentAddress.isEmpty() || moveInTime.isEmpty() || relationship.isEmpty()
                    || workUnit.isEmpty() || remark.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整信息！");
                return;
            }

            String sql = "UPDATE user_info SET name=?, gender=?, birth_date=?, id_card=?, contact_phone=?, " +
                    "household_address=?, current_address=?, move_in_time=?, relationship=?, work_unit=?, remark=? WHERE id=?";
            try {
                int result = db.execute(sql, name, gender, birthDate, idCard, contactPhone, householdAddress,
                        currentAddress, moveInTime, relationship, workUnit, remark, userId);

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
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            String sql = "DELETE FROM user_info WHERE id=?";
            try {
                int result = db.execute(sql, userId);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "删除成功！");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        resetBtn.addActionListener(e -> {
            nameField.setText("");
            genderField.setText("");
            birthDateField.setText("");
            idCardField.setText("");
            contactPhoneField.setText("");
            householdAddressField.setText("");
            currentAddressField.setText("");
            moveInTimeField.setText("");
            relationshipField.setText("");
            workUnitField.setText("");
            remarkField.setText("");
        });

        // 默认加载所有数据到表格中
        refreshTable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserInfoPage userInfoPage = new UserInfoPage();
            userInfoPage.setVisible(true);
        });
    }

    private void refreshTable() {
        try {
            List<UserInfo> allData = db.query("SELECT * FROM user_info", UserInfo.class);
            updateTable(allData);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "数据加载失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<UserInfo> results) {
        tableModel.setRowCount(0);
        if (results != null) {
            for (UserInfo info : results) {
                tableModel.addRow(new Object[] {
                    info.getname(), // 修改: 将 getUserName() 替换为 getName()
                    info.getgender(), info.getbirthdate(), info.getidcard(),
                    info.getcontactphone(), info.gethouseholdaddress(), info.getcurrentaddress(),
                    info.getmoveIntime(), info.getrelationship(), info.getworkunit(), info.getremark()
                });
            }
        }
    }
}