package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import db.MySql;
import java.sql.SQLException;
import java.util.List;

public class QueryChargeRate extends JFrame {
    private MySql mySql; // 数据库连接实例

    public QueryChargeRate() {
        mySql = new MySql(); // 初始化数据库连接
        initUI();
    }

    private void initUI() {
        setTitle("查询收费单价");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建面板并设置布局
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // 中间：收费单价清单
        JPanel middlePanel = new JPanel(new BorderLayout());

        // 从数据库查询收费单价数据
        List<Entity> modifycharge_ratepage = getChargeRatesFromDatabase();

        // 定义表格列名
        String[] columnNames = {"收费项目", "单位：元"};

        // 如果没有从数据库获取到数据，则显示错误信息并关闭窗口
        if (modifycharge_ratepage == null || modifycharge_ratepage.isEmpty()) {
            JOptionPane.showMessageDialog(this, "无法从数据库获取收费单价数据。", "错误", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // 创建表格模型并设置数据
        Object[][] data = new Object[modifycharge_ratepage.size()][2];
        for (int i = 0; i < modifycharge_ratepage.size(); i++) {
            Entity rate = modifycharge_ratepage.get(i);
            data[i][0] = rate.get("item");
            data[i][1] = rate.get("price");
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        middlePanel.add(scrollPane, BorderLayout.CENTER);

        // 底部：按钮区域
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("返回");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭当前窗口
            }
        });
        bottomPanel.add(backButton);

        // 布局组合
        contentPane.add(middlePanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }

    // 从数据库查询收费单价数据
    private List<Entity> getChargeRatesFromDatabase() {
        try {
            return mySql.use().query("SELECT * FROM modifycharge_ratepage", Entity.create());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "数据库查询错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

 public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            QueryChargeRate ex = new QueryChargeRate();
            ex.setVisible(true);
        });
    }
}
