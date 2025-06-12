package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.border.EmptyBorder;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;
import dao.entity.Building;
import dao.entity.CommunityInfo;
import dao.entity.MeterReading;

public class UserBillingReport extends JFrame {

    private JTable table;
    private JComboBox<String> roomIdField; // 修改为 JComboBox<String>
    private JTextField dateField;   // 保留日期字段
    private JComboBox<String> buildingComboBox; // 楼宇选择下拉框
    private JComboBox<String> communityComboBox; // 小区选择下拉框
    private Db db;

    public UserBillingReport() {
        this.db = PropCore.INS.getMySql().use();

        setTitle("User Billing Report");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 输入面板 - 放在NORTH区域
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // 行 0：小区选择
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("选择小区:"), gbc);
        gbc.gridx = 1;
        communityComboBox = new JComboBox<>();
        communityComboBox.addItem("选择小区");
        inputPanel.add(communityComboBox, gbc);

        // 行 1：楼宇选择
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("选择楼宇:"), gbc);
        gbc.gridx = 1;
        buildingComboBox = new JComboBox<>();
        buildingComboBox.addItem("选择楼宇");
        inputPanel.add(buildingComboBox, gbc);

        // 行 2：房间ID + 日期
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("房间ID:"), gbc);
        gbc.gridx = 1;
        roomIdField = new JComboBox<>(); // 初始化为 JComboBox<String>
        roomIdField.setPreferredSize(new Dimension(150, 25)); // 设置固定宽度
        inputPanel.add(roomIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("日期:"), gbc);
        gbc.gridx = 3;
        dateField = new JTextField();
        dateField.setPreferredSize(new Dimension(150, 25)); // 设置固定宽度
        inputPanel.add(dateField, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // 创建新的面板用于表格和按钮 - 放在CENTER区域
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // 表格初始化
        table = new JTable();
        table.setRowHeight(25); // 设置合适的行高
        table.setShowGrid(true); // 显示网格线
        table.setIntercellSpacing(new Dimension(1, 1)); // 设置单元格间距
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 始终显示垂直滚动条
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // 始终显示水平滚动条
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 按钮面板 - 放在SOUTH区域
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10)); // 增加水平和垂直间距
        JButton queryButton = new JButton("查询");
        
        // 操作按钮
        JButton addButton = new JButton("新增");
        JButton editButton = new JButton("修改");
        JButton deleteButton = new JButton("删除");
        
        // 创建单独的按钮面板用于分组
        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionButtons.add(addButton);
        actionButtons.add(editButton);
        actionButtons.add(deleteButton);
        
        // 将组件添加到主按钮面板
        buttonPanel.add(queryButton);
        buttonPanel.add(Box.createHorizontalStrut(20)); // 添加水平间距
        buttonPanel.add(actionButtons);
        
        // 添加边框间距
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // 添加边距防止内容贴边
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 将mainPanel添加到窗口中心区域
        add(mainPanel, BorderLayout.CENTER);

        // 初始化小区下拉框
        loadCommunities();

        // 查询按钮事件
        queryButton.addActionListener((ActionEvent e) -> {
            String roomId = (String) roomIdField.getSelectedItem(); // 获取选中的房间ID
            String date = dateField.getText().trim();

            if (roomId == null || roomId.isEmpty() && date.isEmpty()) {
                JOptionPane.showMessageDialog(UserBillingReport.this,
                        "请至少输入一个查询条件", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loadFeesData(roomId, date); // 调用loadFeesData
        });

        // 小区选择事件
        communityComboBox.addActionListener(e -> {
            String selectedCommunity = (String) communityComboBox.getSelectedItem();
            if (selectedCommunity != null && !selectedCommunity.equals("选择小区")) {
                loadBuildings(selectedCommunity);
                roomIdField.removeAllItems();
                dateField.setText("");
                table.setModel(new DefaultTableModel());
            } else {
                buildingComboBox.removeAllItems();
                buildingComboBox.addItem("选择楼宇");
                roomIdField.removeAllItems();
                dateField.setText("");
                table.setModel(new DefaultTableModel());
            }
            
            // 当小区选择变化时自动触发查询
            if(selectedCommunity != null && !selectedCommunity.equals("选择小区")) {
                String roomId = (String) roomIdField.getSelectedItem();
                String date = dateField.getText().trim();
                if(roomId != null && !roomId.isEmpty()) {
                    loadFeesData(roomId, date);
                }
            }
        });

        // 楼宇选择事件
        buildingComboBox.addActionListener(e -> {
            String selectedBuilding = (String) buildingComboBox.getSelectedItem();
            if (selectedBuilding != null && !selectedBuilding.equals("选择楼宇")) {
                loadRoomIds(selectedBuilding);
                dateField.setText("");
                table.setModel(new DefaultTableModel());
            } else {
                roomIdField.removeAllItems();
                dateField.setText("");
                table.setModel(new DefaultTableModel());
            }
            
            // 当楼宇选择变化时自动触发查询
            if(selectedBuilding != null && !selectedBuilding.equals("选择楼宇")) {
                String roomId = (String) roomIdField.getSelectedItem();
                String date = dateField.getText().trim();
                if(roomId != null && !roomId.isEmpty()) {
                    loadFeesData(roomId, date);
                }
            }
        });

        // 房间ID选择事件
        roomIdField.addActionListener(e -> {
            // 清空日期字段并重置表格
            dateField.setText("");
            table.setModel(new DefaultTableModel());
            
            // 当房间选择变化时自动触发查询
            String roomId = (String) roomIdField.getSelectedItem();
            String date = dateField.getText().trim();
            if(roomId != null && !roomId.isEmpty()) {
                loadFeesData(roomId, date);
            }
        });

        setVisible(true);
        //this.loadFeesData("", ""); // 默认不加载所有数据，需选择楼宇后再加载
    }

    // 加载小区信息方法
    private void loadCommunities() {
        List<Entity> communities = new CommunityInfo().loadAll();
        communityComboBox.removeAllItems();
        communityComboBox.addItem("选择小区");
        for (Entity community : communities) {
            communityComboBox.addItem(community.getStr("district_id"));
        }
    }

    // 加载楼宇方法支持按小区过滤
    private void loadBuildings(String communityId) {
        Building build = new Building();
        List<Entity> buildings = build.loadAll(build.getQueryEntity("district_id", communityId));
        buildingComboBox.removeAllItems();
        buildingComboBox.addItem("选择楼宇");
        for (Entity building : buildings) {
            buildingComboBox.addItem(building.getStr("building_id"));
        }
    }

    // 根据楼宇ID加载房间号
    private void loadRoomIds(String buildingId) {
        MeterReading meterReading = new MeterReading();
        List<Entity> rooms = meterReading.loadAll(meterReading.getQueryEntity("building_id", buildingId));;
        String[] roomIds = rooms.stream().map(entity -> entity.getStr("room_number")).toArray(String[]::new);
        roomIdField.setModel(new DefaultComboBoxModel<>(roomIds));
    }

    // 查询方法
    private void loadFeesData(String roomId, String date) {

        String[] columnNames = {"房间ID", "日期", "水表读数", "电费读数", "燃气读数"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        MeterReading  meterReading = new MeterReading();
        Entity query = meterReading.getEntity();

        if (!roomId.isEmpty()) {
            query.set("room_number", roomId);
        }
        if (!date.isEmpty()) {
            query.set("input_date", date + "%");
        }

        List<Entity> feesList = meterReading.loadAll(query);

        for (Entity entity : feesList) {
            model.addRow(new Object[]{
                    entity.getStr("room_number"),
                    entity.getStr("date"),
                    entity.getDouble("water_reading"),
                    entity.getDouble("electric_reading"),
                    entity.getDouble("gas_reading")
            });
        }

        table.setModel(model);
    }
}