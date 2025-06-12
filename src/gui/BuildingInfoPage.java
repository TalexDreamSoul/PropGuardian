package gui;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;
import dao.entity.Building;
import dao.entity.UserInfo;
import utils.MentionUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BuildingInfoPage extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField[] fields;
    private final String[] labels = {"小区ID", "楼宇编号", "层数", "面积", "高度", "类型", "状态"};

    public BuildingInfoPage() {
        setTitle("楼宇信息维护");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 表格初始化
        tableModel = new DefaultTableModel(labels, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 表单区域优化：使用 Box + Grid 更美观
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        fields = new JTextField[labels.length];

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (int i = 0; i < labels.length; i++) {
            JPanel fieldPanel = new JPanel(new BorderLayout(5, 5));
            fields[i] = new JTextField(10);
            fieldPanel.add(new JLabel(labels[i]), BorderLayout.NORTH);
            fieldPanel.add(fields[i], BorderLayout.CENTER);
            if (i < 4) {
                row1.add(fieldPanel);
            } else {
                row2.add(fieldPanel);
            }
        }
        formPanel.add(row1);
        formPanel.add(row2);

        // 按钮区域
        JPanel buttonPanel = new JPanel();
        String[] btnNames = {"添加", "修改", "删除", "查询", "重置", "刷新"};
        JButton[] buttons = new JButton[btnNames.length];
        for (int i = 0; i < btnNames.length; i++) {
            buttons[i] = new JButton(btnNames[i]);
            buttonPanel.add(buttons[i]);
        }

        // 南部组合面板
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // 数据加载
        loadData();

        // 表格选中行同步表单
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setText(table.getValueAt(row, i).toString());
                }
            }
        });

        // 按钮事件绑定
        buttons[0].addActionListener(e -> addBuilding());
        buttons[1].addActionListener(e -> updateBuilding());
        buttons[2].addActionListener(e -> deleteBuilding());
        buttons[3].addActionListener(e -> queryBuilding());
        buttons[4].addActionListener(e -> resetForm());
        buttons[5].addActionListener(e -> loadData());

        setVisible(true);
    }

    private void loadData() {
        updateTable(new Building().loadAll());
    }

    private void updateTable(List<Entity> list) {
        tableModel.setRowCount(0);
        for (Entity e : list) {
            tableModel.addRow(new Object[]{
                    e.getStr("district_id"),
                    e.getStr("building_id"),
                    e.getInt("total_storey"),
                    e.getDouble("total_area"),
                    e.getDouble("height"),
                    e.getStr("type"),
                    e.getStr("status")
            });
        }
    }

    private void addBuilding( ) {
        try {
            double totalArea = Double.parseDouble(getField(3));
            if (totalArea < 0) { // 添加面积校验
                showError("面积不能为负数，请输入正确的面积值");
                return;
            }

            Building building = new Building(
                    Short.parseShort(getField(0)),
                    Short.parseShort(getField(1)),
                    Short.parseShort(getField(2)),
                    totalArea,
                    Double.parseDouble(getField(4)),
                    Short.parseShort(getField(5)),
                    getField(6)
            );

            building.storage();

            loadData();
            resetForm();
        } catch (Exception e) {
            showError("添加失败", e);
        }
    }

    private void updateBuilding() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showMessage("请选择要修改的行");
            return;
        }
        try {
            double area = Double.parseDouble(getField(3));
            if (area < 0) { // 添加面积校验
                showError("面积不能为负数，请输入正确的面积值");
                return;
            }
            Building building = new Building();
            Entity update = building.getEntity()
                    .set("total_storey", Integer.parseInt(getField(2)))
                    .set("total_area", area)
                    .set("height", Double.parseDouble(getField(4)))
                    .set("type", getField(5))
                    .set("status", getField(6));
            Entity where = Entity.create()
                    .set("district_id", getField(0))
                    .set("building_id", getField(1));
            building.getMySql().use().update(update, where);
            loadData();
        } catch (Exception e) {
            showError("修改失败", e);
        }
    }

    private void deleteBuilding() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showMessage("请选择要删除的行");
            return;
        }
        try {
            Building building = new Building();
            Entity set = building.getEntity()
                    .set("district_id", table.getValueAt(row, 0))
                    .set("building_id", table.getValueAt(row, 1));
            int rowDeleted = building.getMySql().use().del(set);
            MentionUtil.mentionForDelete(rowDeleted > 0, this, this::loadData);
        } catch (Exception e) {
            showError("删除失败", e);
        }
    }

    private void queryBuilding() {
        String districtId = getField(0);
        if (districtId.isEmpty()) {
            showMessage("请输入小区ID");
            return;
        }

        updateTable(new Building().loadAll(new Building().getQueryEntity("district_id", districtId)));
    }

    private void resetForm() {
        for (JTextField f : fields) f.setText("");
    }

    private String getField(int index) {
        return fields[index].getText().trim();
    }

    private void showError(String title) {
        JOptionPane.showMessageDialog(this, title, "错误", JOptionPane.ERROR_MESSAGE);
    }

    private void showError(String title, Exception e) {
        JOptionPane.showMessageDialog(this, title + ": " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(BuildingInfoPage::new);
//    }
}
