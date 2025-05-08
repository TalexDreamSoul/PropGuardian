package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QueryChargeRate extends JFrame {

    public QueryChargeRate() {
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
        String[] columnNames = {"收费项目", "单位：元"};
        Object[][] data = {
                {"水费", "3.00"},
                {"电费", "0.50"},
                {"燃气费", "4.00"}
        };
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

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            QueryChargeRate ex = new QueryChargeRate();
            ex.setVisible(true);
        });
    }
}
