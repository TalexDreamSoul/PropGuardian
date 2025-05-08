package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HouseInfoPage extends JFrame {
    private JTable table;
    private JTextField communityNoField, buildingNoField, houseNoField, propertyAreaField;
    private JTextField nameField, genderField, idCardField, contactAddressField, contactPhoneField;

    public HouseInfoPage() {
        setTitle("业主信息维护");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create table
        String[] columnNames = {"小区号", "楼号", "房号", "产权面积", "房屋状态", "用途"};
        Object[][] data = {}; // Initial empty data
        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create text fields
        communityNoField = new JTextField(10);
        buildingNoField = new JTextField(10);
        houseNoField = new JTextField(10);
        propertyAreaField = new JTextField(10);
        nameField = new JTextField(10);
        genderField = new JTextField(10);
        idCardField = new JTextField(10);
        contactAddressField = new JTextField(10);
        contactPhoneField = new JTextField(10);

        // Create buttons
        JButton queryByNameBtn = new JButton("姓名查询");
        JButton addBtn = new JButton("添加");
        JButton modifyBtn = new JButton("修改");
        JButton deleteBtn = new JButton("删除");
        JButton resetBtn = new JButton("重置");
        JButton updateBtn = new JButton("更新");

        // Layout
        JPanel inputPanel = new JPanel(new GridLayout(3, 4));
        inputPanel.add(new JLabel("小区号"));
        inputPanel.add(communityNoField);
        inputPanel.add(new JLabel("楼号"));
        inputPanel.add(buildingNoField);
        inputPanel.add(new JLabel("房号"));
        inputPanel.add(houseNoField);
        inputPanel.add(new JLabel("产权面积"));
        inputPanel.add(propertyAreaField);
        inputPanel.add(new JLabel("姓名"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("性别"));
        inputPanel.add(genderField);
        inputPanel.add(new JLabel("身份证"));
        inputPanel.add(idCardField);
        inputPanel.add(new JLabel("联系地址"));
        inputPanel.add(contactAddressField);
        inputPanel.add(new JLabel("联系电话"));
        inputPanel.add(contactPhoneField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(queryByNameBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(modifyBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(updateBtn);

        // Add components to frame
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to buttons (example implementation)
        queryByNameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement query by name functionality
            }
        });

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement add functionality
            }
        });

        modifyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement modify functionality
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement delete functionality
            }
        });

        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement reset functionality
            }
        });

        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement update functionality
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HouseInfoPage houseInfoPage = new HouseInfoPage();
            houseInfoPage.setVisible(true);
        });
    }
}
