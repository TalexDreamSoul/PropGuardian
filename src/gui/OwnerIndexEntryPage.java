package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//导入 Hutool 工具库中的数据库操作类，用于简化数据库插入、查询等操作。
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import core.PropCore;

public class OwnerIndexEntryPage extends JFrame {
    private JTextField inputDateField;
    private JTextField waterReadingField1;
    private JTextField electricReadingField1;
    private JTextField gasReadingField1;
    private JTextField waterReadingField2;
    private JTextField electricReadingField2;
    private JTextField gasReadingField2;
    private JButton submitButton;
    private JButton cancelButton;
    private JButton queryButton;  // 确保 queryButton 在类的顶部声明
    private Db db;

    // 在类顶部添加字段
    private String community;
    private String building;

    public OwnerIndexEntryPage(String community, String building) {
        this.community = community;
        this.building = building;

        setTitle("业主水/电/气指数录入");
        setSize(700, 300);
        setLocationRelativeTo(null);//设置窗口在屏幕中央显示。
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置关闭窗口时释放资源。

        db = PropCore.INS.getMySql().use();//获取数据库连接实例，准备进行数据库操作。

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel inputDateLabel = new JLabel("日期(YYYY-MM)");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(inputDateLabel, gbc);

        inputDateField = new JTextField(10);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(inputDateField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 1;
        add(new JLabel("水表读数"), gbc);

        gbc.gridx = 2;
        add(new JLabel("电表读数"), gbc);

        gbc.gridx = 3;
        add(new JLabel("气表读数"), gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("1101"), gbc);

        waterReadingField1 = new JTextField(8);
        gbc.gridx = 1;
        add(waterReadingField1, gbc);

        electricReadingField1 = new JTextField(8);
        gbc.gridx = 2;
        add(electricReadingField1, gbc);

        gasReadingField1 = new JTextField(8);
        gbc.gridx = 3;
        add(gasReadingField1, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("1102"), gbc);

        waterReadingField2 = new JTextField(8);
        gbc.gridx = 1;
        add(waterReadingField2, gbc);

        electricReadingField2 = new JTextField(8);
        gbc.gridx = 2;
        add(electricReadingField2, gbc);

        gasReadingField2 = new JTextField(8);
        gbc.gridx = 3;
        add(gasReadingField2, gbc);

        submitButton = new JButton("提交");
        cancelButton = new JButton("取消");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(buttonPanel, gbc);

        submitButton.addActionListener(e -> handleSubmit());

        cancelButton.addActionListener(e -> handleCancel());

        // 新增查询按钮
        queryButton = new JButton("查询");
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        add(queryButton, gbc);

        // 查询按钮事件监听器
        queryButton.addActionListener(e -> handleQuery());

        setVisible(true);
    }

    // 添加验证方法
    private boolean validateInput() {
        String inputDate = inputDateField.getText().trim();

        if (inputDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "日期不能为空", "输入错误", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 修改日期格式验证为 YYYY-MM
        if (!inputDate.matches("\\d{4}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "日期格式应为YYYY-MM", "输入错误", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 验证读数是否为有效数字
        try {
            Double.parseDouble(waterReadingField1.getText());
            Double.parseDouble(electricReadingField1.getText());
            Double.parseDouble(gasReadingField1.getText());
            Double.parseDouble(waterReadingField2.getText());
            Double.parseDouble(electricReadingField2.getText());
            Double.parseDouble(gasReadingField2.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的数字格式", "输入错误", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void handleSubmit() {
        if (!validateInput()) {
            return;
        }

        String inputDate = inputDateField.getText() + "-01";
        String waterReading1 = waterReadingField1.getText();
        String electricReading1 = electricReadingField1.getText();
        String gasReading1 = gasReadingField1.getText();
        String waterReading2 = waterReadingField2.getText();
        String electricReading2 = electricReadingField2.getText();
        String gasReading2 = gasReadingField2.getText();

        saveDataToDatabase(inputDate, waterReading1, electricReading1, gasReading1, waterReading2, electricReading2, gasReading2);
    }

    private void saveDataToDatabase(String inputDate, String waterReading1, String electricReading1, String gasReading1, String waterReading2, String electricReading2, String gasReading2) {
        try {
            // 插入1101房间数据
            Entity entity1 = Entity.create("meter_reading")
                    .set("input_date", inputDate)
                    .set("room_number", "1101")
                    .set("water_reading", Double.parseDouble(waterReading1))
                    .set("electric_reading", Double.parseDouble(electricReading1))
                    .set("gas_reading", Double.parseDouble(gasReading1));

            // 添加社区和楼宇信息，确保它们是有效的数字字符串
            if (community != null && !community.isEmpty() && community.matches("\\d+")) {
                entity1.set("district_id", Integer.parseInt(community));
            }
            if (building != null && !building.isEmpty() && building.matches("\\d+")) {
                entity1.set("building_id", Integer.parseInt(building));
            }

            db.insert(entity1);

            // 插入1102房间数据
            Entity entity2 = Entity.create("meter_reading")
                    .set("input_date", inputDate)
                    .set("room_number", "1102")
                    .set("water_reading", Double.parseDouble(waterReading2))
                    .set("electric_reading", Double.parseDouble(electricReading2))
                    .set("gas_reading", Double.parseDouble(gasReading2));

            // 添加社区和楼宇信息，确保它们是有效的数字字符串
            if (community != null && !community.isEmpty() && community.matches("\\d+")) {
                entity2.set("district_id", Integer.parseInt(community));
            }
            if (building != null && !building.isEmpty() && building.matches("\\d+")) {
                entity2.set("building_id", Integer.parseInt(building));
            }

            db.insert(entity2);

            JOptionPane.showMessageDialog(this, "数据已成功保存到数据库！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            // 捕获详细异常信息并显示
            JOptionPane.showMessageDialog(this, "保存失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // 打印堆栈信息以便调试
        }
    }

    private void handleCancel() {
        dispose();
    }

    // 新增查询按钮处理方法
    private void handleQuery() {
        dispose(); // 关闭当前页面
        new IndexManagementPage(community, building); // 跳转到新页面
    }

}