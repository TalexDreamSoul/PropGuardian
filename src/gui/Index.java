package gui;

import javax.swing.*;
import java.awt.*;

public class Index extends JFrame {
    public Index() {
        setTitle("Property Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 创建导航条
        JMenuBar menuBar = new JMenuBar();

        JMenu manageMenu = new JMenu("系统管理");

        menuBar.add(manageMenu);

        // 添加基本信息菜单及其子菜单项
        JMenu basicInfoMenu = new JMenu("基本信息");
        JMenuItem communityInfoItem = new JMenuItem("小区信息维护");
        JMenuItem buildingInfoItem = new JMenuItem("楼宇信息维护");
        JMenuItem houseInfoItem = new JMenuItem("房屋信息维护");
        JMenuItem modifyRateItem = new JMenuItem("修改收费单价");
        JMenuItem queryRateItem = new JMenuItem("查询收费单价");
        basicInfoMenu.add(communityInfoItem);
        basicInfoMenu.add(buildingInfoItem);
        basicInfoMenu.add(houseInfoItem);
        basicInfoMenu.add(modifyRateItem);
        basicInfoMenu.add(queryRateItem);
        menuBar.add(basicInfoMenu);

        // 添加消费指数菜单及其子菜单项
        JMenu consumptionIndexMenu = new JMenu("消费指数");
        JMenuItem ownerIndexItem = new JMenuItem("业主水电气指数录入");
        JMenuItem publicIndexItem = new JMenuItem("公共水电指数录入");
        consumptionIndexMenu.add(ownerIndexItem);
        consumptionIndexMenu.add(publicIndexItem);
        menuBar.add(consumptionIndexMenu);

        // 添加费用报表菜单及其子菜单项
        JMenu reportsMenu = new JMenu("费用报表");
        JMenuItem electricityBillItem = new JMenuItem("电费收费报表");
        JMenuItem waterBillItem = new JMenuItem("水费收费报表");
        JMenuItem gasBillItem = new JMenuItem("气费收费报表");
        JMenuItem userBillItem = new JMenuItem("用户收费报表");
        JMenuItem propertyBillItem = new JMenuItem("物业收费报表");
        reportsMenu.add(electricityBillItem);
        reportsMenu.add(waterBillItem);
        reportsMenu.add(gasBillItem);
        reportsMenu.add(userBillItem);
        reportsMenu.add(propertyBillItem);
        menuBar.add(reportsMenu);



        // 添加 Exit 菜单项到菜单栏
        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.addActionListener(e -> System.exit(0));
        menuBar.add(exitItem);

        setJMenuBar(menuBar);

        // 添加主内容面板
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(new JLabel("Welcome to the Property Management System!", SwingConstants.CENTER), BorderLayout.CENTER);
        add(contentPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Index index = new Index();
            index.setVisible(true);
        });
    }
}