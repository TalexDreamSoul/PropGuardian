package gui;

import javax.swing.*;
import java.awt.*;

public class SelectCommunityAndBuilding extends JFrame {
    private JComboBox<String> communityComboBox;
    private JComboBox<String> buildingComboBox;
    private JButton confirmButton;
    private JButton backButton;

    public SelectCommunityAndBuilding() {
        setTitle("选择小区和楼宇");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // 设置布局为 GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 组件之间的间距

        // 添加标签和下拉框
        JLabel communityLabel = new JLabel("选择小区");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST; // 标签靠右对齐
        add(communityLabel, gbc);

        communityComboBox = new JComboBox<>(new String[]{"选择小区"});
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST; // 下拉框靠左对齐
        add(communityComboBox, gbc);

        JLabel buildingLabel = new JLabel("选择楼宇");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST; // 标签靠右对齐
        add(buildingLabel, gbc);

        buildingComboBox = new JComboBox<>(new String[]{"选择楼宇"});
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST; // 下拉框靠左对齐
        add(buildingComboBox, gbc);

        // 创建一个面板来容纳并排的按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(5, 5, 5, 5);

        // 添加按钮到面板
        confirmButton = new JButton("确定");
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonPanel.add(confirmButton, buttonGbc);

        backButton = new JButton("返回");
        buttonGbc.gridx = 1;
        buttonGbc.gridy = 0;
        buttonPanel.add(backButton, buttonGbc);

        // 将按钮面板添加到主窗口
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // 面板占据两列
        gbc.anchor = GridBagConstraints.CENTER; // 面板居中对齐
        add(buttonPanel, gbc);

        // 添加按钮事件监听器
        confirmButton.addActionListener(e -> onConfirm());
        backButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void onConfirm() {
        String selectedCommunity = (String) communityComboBox.getSelectedItem();
        String selectedBuilding = (String) buildingComboBox.getSelectedItem();

        if ("选择小区".equals(selectedCommunity) || "选择楼宇".equals(selectedBuilding)) {
            JOptionPane.showMessageDialog(this, "请选择有效的小区和楼宇", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 处理确认逻辑，例如跳转到下一个页面或执行其他操作
        JOptionPane.showMessageDialog(this, "选择了: 小区 - " + selectedCommunity + ", 楼宇 - " + selectedBuilding, "成功", JOptionPane.INFORMATION_MESSAGE);
    }
}


