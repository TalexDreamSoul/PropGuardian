package gui;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;
import dao.entity.OwnerInfo;
import dao.entity.UserInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OwnerInfoPage extends JFrame {
    private JTable table;
    private Db db;
    private DefaultTableModel tableModel;
    private JTextField communityNoField, buildingNoField, houseNoField, propertyAreaField;
    private JTextField nameField, genderField, idCardField, contactAddressField, contactPhoneField;

    public OwnerInfoPage() {
        setTitle("业主信息管理");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);


        this.db = PropCore.INS.getMySql().use();


        String[] columnNames = {"小区号", "楼号", "房号", "产权面积", "状态", "用途", "姓名", "性别", "身份证", "联系地址", "联系电话"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        communityNoField = new JTextField(10);
        buildingNoField = new JTextField(10);
        houseNoField = new JTextField(10);
        propertyAreaField = new JTextField(10);
        nameField = new JTextField(10);
        genderField = new JTextField(10);
        idCardField = new JTextField(10);
        contactAddressField = new JTextField(10);
        contactPhoneField = new JTextField(10);

        JButton queryByNameBtn = new JButton("姓名查询");
        JButton addBtn = new JButton("添加");
        JButton modifyBtn = new JButton("修改");
        JButton deleteBtn = new JButton("删除");
        JButton resetBtn = new JButton("重置");

        JPanel inputPanel = new JPanel(new GridLayout(3, 4));
        inputPanel.add(new JLabel("小区号")); inputPanel.add(communityNoField);
        inputPanel.add(new JLabel("楼号")); inputPanel.add(buildingNoField);
        inputPanel.add(new JLabel("房号")); inputPanel.add(houseNoField);
        inputPanel.add(new JLabel("产权面积")); inputPanel.add(propertyAreaField);
        inputPanel.add(new JLabel("姓名")); inputPanel.add(nameField);
        inputPanel.add(new JLabel("性别")); inputPanel.add(genderField);
        inputPanel.add(new JLabel("身份证")); inputPanel.add(idCardField);
        inputPanel.add(new JLabel("联系地址")); inputPanel.add(contactAddressField);
        inputPanel.add(new JLabel("联系电话")); inputPanel.add(contactPhoneField);

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

            OwnerInfo ownerInfo = new OwnerInfo();
            Entity oname = ownerInfo.getQueryEntity("oname", name);
            List<OwnerInfo> results = ownerInfo.loadAllByEntity(oname, OwnerInfo.class);
            updateTable(results);
        });

        addBtn.addActionListener(e -> {
            String communityNo = communityNoField.getText();
            String buildingNo = buildingNoField.getText();
            String houseNo = houseNoField.getText();
            String propertyArea = propertyAreaField.getText();
            String name = nameField.getText();
            String gender = genderField.getText();
            String idCard = idCardField.getText();
            String contactAddress = contactAddressField.getText();
            String contactPhone = contactPhoneField.getText();

            if (communityNo.isEmpty() || buildingNo.isEmpty() || houseNo.isEmpty() || propertyArea.isEmpty()
                    || name.isEmpty() || gender.isEmpty() || idCard.isEmpty() || contactAddress.isEmpty() || contactPhone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整信息！");
                return;
            }

            OwnerInfo ownerInfo = new OwnerInfo(Short.parseShort(communityNo),
                    Short.parseShort(buildingNo),
                    Short.parseShort(houseNo),
                    Double.parseDouble(propertyArea),
                    "active",
                    "res",
                    name,
                    gender,
                    idCard,
                    contactAddress,
                    contactPhone);
            String sql = "INSERT INTO owner_info(district_id, building_id, room_id, area, status, purpose, oname, sex, id_Num, address, phone) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            try {
                int result = db.execute(sql,
                        Integer.parseInt(communityNo),
                        Integer.parseInt(buildingNo),
                        Integer.parseInt(houseNo),
                        Double.parseDouble(propertyArea),
                        "active",
                        "res",
                        name,
                        gender,
                        idCard,
                        contactAddress,
                        contactPhone);

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

            int ownerId = (int) tableModel.getValueAt(selectedRow, 0);
            String communityNo = communityNoField.getText();
            String buildingNo = buildingNoField.getText();
            String houseNo = houseNoField.getText();
            String propertyArea = propertyAreaField.getText();
            String name = nameField.getText();
            String gender = genderField.getText();
            String idCard = idCardField.getText();
            String contactAddress = contactAddressField.getText();
            String contactPhone = contactPhoneField.getText();

            if (communityNo.isEmpty() || buildingNo.isEmpty() || houseNo.isEmpty() || propertyArea.isEmpty()
                    || name.isEmpty() || gender.isEmpty() || idCard.isEmpty() || contactAddress.isEmpty() || contactPhone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整信息！");
                return;
            }

            OwnerInfo ownerInfo = new OwnerInfo(Short.parseShort(communityNo), Short.parseShort(buildingNo), Short.parseShort(houseNo), Double.parseDouble(propertyArea), "active", "res", name, gender, idCard, contactAddress, contactPhone);
            ownerInfo.updateFixedSelf(this::refreshTable, this);
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "请选择要删除的行！");
                return;
            }
            int district_id = (int) tableModel.getValueAt(selectedRow, 0);
            int building_id = (int) tableModel.getValueAt(selectedRow, 1);
            int room_id = (int) tableModel.getValueAt(selectedRow, 2);
            String sql = "DELETE FROM owner_info WHERE district_id = ? AND building_id = ? AND room_id = ?";
            try {
                int result = db.execute(sql, district_id, building_id, room_id);
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
            communityNoField.setText("");
            buildingNoField.setText("");
            houseNoField.setText("");
            propertyAreaField.setText("");
            nameField.setText("");
            genderField.setText("");
            idCardField.setText("");
            contactAddressField.setText("");
            contactPhoneField.setText("");
        });

        // 默认加载所有数据到表格中
        refreshTable();
    }

    private void refreshTable() {
        updateTable(new OwnerInfo().loadAllByEntity(OwnerInfo.class));
    }

    private void updateTable(List<OwnerInfo> results) {
        tableModel.setRowCount(0);
        if (results != null) {
            for (OwnerInfo info : results) {
                tableModel.addRow(new Object[] {
                        info.getDistrict_id(), info.getBuilding_id(), info.getRoom_id(), info.getArea(),
                        info.getStatus(), info.getPurpose(), info.getOname(), info.getSex(), info.getId_num(),
                        info.getAddress(), String.valueOf(info.getPhone())
                });
            }
        }
    }
}