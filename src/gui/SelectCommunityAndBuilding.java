package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;

import java.util.List;

public class SelectCommunityAndBuilding extends JFrame {
    private JComboBox<String> communityComboBox;
    private JComboBox<String> buildingComboBox;
    private JButton confirmButton;
    private JButton backButton;

    private Db db; // 数据库连接

    public SelectCommunityAndBuilding() {
        // 初始化数据库连接
        this.db = PropCore.INS.getMySql().use();

        setTitle("选择小区和楼宇");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 小区标签和下拉框
        JLabel communityLabel = new JLabel("选择小区");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(communityLabel, gbc);

        communityComboBox = new JComboBox<>();
        communityComboBox.addItem("选择小区");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(communityComboBox, gbc);

        // 楼宇标签和下拉框
        JLabel buildingLabel = new JLabel("选择楼宇");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(buildingLabel, gbc);

        buildingComboBox = new JComboBox<>();
        buildingComboBox.addItem("选择楼宇");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(buildingComboBox, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(5, 5, 5, 5);

        confirmButton = new JButton("确定");
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonPanel.add(confirmButton, buttonGbc);

        backButton = new JButton("返回");
        buttonGbc.gridx = 1;
        buttonGbc.gridy = 0;
        buttonPanel.add(backButton, buttonGbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // 加载小区数据
        loadCommunities();

        // 小区选择变化时更新楼宇列表
        communityComboBox.addActionListener(e -> {
            String selected = (String) communityComboBox.getSelectedItem();
            if (selected != null && !selected.equals("选择小区")) {
                loadBuildings(selected);
            }
        });

        // 确定按钮点击事件
        confirmButton.addActionListener(e -> onConfirm());

        // 返回按钮点击事件
        backButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    // 加载小区列表
    private void loadCommunities() {
        try {
            List<Entity> communities = db.query("SELECT district_name FROM community_info");
            communityComboBox.removeAllItems();
            communityComboBox.addItem("选择小区");
            for (Entity community : communities) {
                communityComboBox.addItem(community.getStr("district_name"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载小区失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // 根据小区加载楼宇
    private void loadBuildings(String communityName) {
        try {
            String sql = "SELECT name FROM building_info WHERE community_name = ?";
            List<Entity> buildings = db.query(sql, communityName);
            buildingComboBox.removeAllItems();
            buildingComboBox.addItem("选择楼宇");
            for (Entity building : buildings) {
                buildingComboBox.addItem(building.getStr("name"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载楼宇失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // 确定按钮逻辑
    private void onConfirm() {
        String selectedCommunity = (String) communityComboBox.getSelectedItem();
        String selectedBuilding = (String) buildingComboBox.getSelectedItem();

        if ("选择小区".equals(selectedCommunity) || "选择楼宇".equals(selectedBuilding)) {
            JOptionPane.showMessageDialog(this, "请选择有效的小区和楼宇", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 跳转到新的录入页面（示例）
        new OwnerIndexEntryPage(selectedCommunity, selectedBuilding).setVisible(true);
        dispose(); // 关闭当前窗口
    }

    // 示例目标页面（可替换为你自己的业务页面）
    static class OwnerIndexEntryPage extends JFrame {
        public OwnerIndexEntryPage(String community, String building) {
            setTitle("业主用水电燃气录入");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTextArea area = new JTextArea("已选择小区：" + community + "\n已选择楼宇：" + building);
            area.setEditable(false);
            add(area, BorderLayout.CENTER);

            JButton backBtn = new JButton("返回");
            backBtn.addActionListener(e -> dispose());
            add(backBtn, BorderLayout.SOUTH);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SelectCommunityAndBuilding());
    }
}
