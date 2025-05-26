package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private Db db;

    public OwnerIndexEntryPage(String community, String building) {
        setTitle("业主水/电/气指数录入");
        setSize(700, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        db = PropCore.INS.getMySql().use();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel inputDateLabel = new JLabel("日期(YYYYMM)");
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

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancel();
            }
        });

        setVisible(true);
    }

    private void handleSubmit() {
        String inputDate = inputDateField.getText();
        String waterReading1 = waterReadingField1.getText();
        String electricReading1 = electricReadingField1.getText();
        String gasReading1 = gasReadingField1.getText();
        String waterReading2 = waterReadingField2.getText();
        String electricReading2 = electricReadingField2.getText();
        String gasReading2 = gasReadingField2.getText();

        saveDataToDatabase(inputDate, waterReading1, electricReading1, gasReading1, waterReading2, electricReading2, gasReading2);

        JOptionPane.showMessageDialog(this, "提交成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveDataToDatabase(String inputDate, String waterReading1, String electricReading1, String gasReading1, String waterReading2, String electricReading2, String gasReading2) {
        try {
            Entity entity1 = Entity.create("meter_reading")
                    .set("input_date", inputDate)
                    .set("room_number", "1101")
                    .set("water_reading", Double.parseDouble(waterReading1))
                    .set("electric_reading", Double.parseDouble(electricReading1))
                    .set("gas_reading", Double.parseDouble(gasReading1));
            db.insert(entity1);

            Entity entity2 = Entity.create("meter_reading")
                    .set("input_date", inputDate)
                    .set("room_number", "1102")
                    .set("water_reading", Double.parseDouble(waterReading2))
                    .set("electric_reading", Double.parseDouble(electricReading2))
                    .set("gas_reading", Double.parseDouble(gasReading2));
            db.insert(entity2);

            JOptionPane.showMessageDialog(this, "数据已成功保存到数据库！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "保存失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancel() {
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OwnerIndexEntryPage("社区示例", "楼宇示例"));
    }
}
