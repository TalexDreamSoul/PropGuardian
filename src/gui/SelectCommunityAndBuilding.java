package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;

import java.util.Collection;
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

        loadCommunities();

        communityComboBox.addActionListener(e -> {
            String selected = (String) communityComboBox.getSelectedItem();
            if (selected != null && !selected.equals("选择小区")) {
                List<String> buildings = getBuildingFromCommunityId(selected);

                buildingComboBox.removeAllItems();
                buildingComboBox.addItem("选择楼宇");
                if (buildings != null) {
                    for (String building : buildings) {
                        buildingComboBox.addItem(building);
                    }
                }
            }
        });

        confirmButton.addActionListener(e -> onConfirm());

        backButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void loadCommunities() {
        try {
            List<Entity> communities = db.query("SELECT district_id FROM community_info");
            communityComboBox.removeAllItems();
            communityComboBox.addItem("选择小区");
            for (Entity community : communities) {
                communityComboBox.addItem(community.getStr("district_id"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载小区失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void onConfirm() {
        String selectedCommunity = (String) communityComboBox.getSelectedItem();
        String selectedBuilding = (String) buildingComboBox.getSelectedItem();

        if ("选择小区".equals(selectedCommunity) || "选择楼宇".equals(selectedBuilding)) {
            JOptionPane.showMessageDialog(this, "请选择有效的小区和楼宇", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new OwnerIndexEntryPage(selectedCommunity, selectedBuilding).setVisible(true);
        dispose(); 
    }

    private List<String> getBuildingFromCommunityId(String communityId) {
        try {
            String sql = "SELECT * FROM building_info WHERE district_id = ?";
            List<Entity> results = db.query(sql, communityId);

            return results.stream().map(item -> String.valueOf(item.get("building_id"))).toList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "获取小区ID失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SelectCommunityAndBuilding());
    }
}
