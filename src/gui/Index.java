package gui;

import javax.swing.*;
import java.awt.*;

import gui.WaterBillingReport;

public class Index extends JFrame {

    public Index() {
        setTitle("Property Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create navigation bar
        JMenuBar menuBar = new JMenuBar();

        JMenu manageMenu = new JMenu("System Management");
        menuBar.add(manageMenu);

        // Add basic information menu and its sub-menu items
        JMenu basicInfoMenu = new JMenu("Basic Information");
        JMenuItem communityInfoItem = new JMenuItem("Community Info Maintenance");
        JMenuItem buildingInfoItem = new JMenuItem("Building Info Maintenance");
        JMenuItem houseInfoItem = new JMenuItem("House Info Maintenance");
        JMenuItem modifyRateItem = new JMenuItem("Modify Charge Rate");
        JMenuItem queryRateItem = new JMenuItem("Query Charge Rate");
        basicInfoMenu.add(communityInfoItem);
        basicInfoMenu.add(buildingInfoItem);
        basicInfoMenu.add(houseInfoItem);
        basicInfoMenu.add(modifyRateItem);
        basicInfoMenu.add(queryRateItem);
        menuBar.add(basicInfoMenu);

        // Add action listener to "Community Info Maintenance" menu item
        communityInfoItem.addActionListener(e -> {
            CommunityInfoPage communityInfoPage = new CommunityInfoPage();
            communityInfoPage.setVisible(true);
        });
        buildingInfoItem.addActionListener(e -> {
            new BuildingInfoPage().setVisible(true);
        });
        modifyRateItem.addActionListener(e -> {
            ModifyChargeRatePage modifyChargeRatePage = new ModifyChargeRatePage();
            modifyChargeRatePage.setVisible(true);
        });
        // Add action listener to "Query Charge Rate" menu item
        queryRateItem.addActionListener(e -> {
            QueryChargeRate queryChargeRate = new QueryChargeRate();
            queryChargeRate.setVisible(true);
        });

        // Add consumption index menu and its sub-menu items
        JMenu consumptionIndexMenu = new JMenu("Consumption Index");
        JMenuItem ownerIndexItem = new JMenuItem("Owner Water/Electricity/Gas Index Entry");
        JMenuItem publicIndexItem = new JMenuItem("Public Water/Electricity Index Entry");
        consumptionIndexMenu.add(ownerIndexItem);
        consumptionIndexMenu.add(publicIndexItem);
        menuBar.add(consumptionIndexMenu);

        // Add fee report menu and its sub-menu items
        JMenu reportsMenu = new JMenu("Fee Reports");
        JMenuItem electricityBillItem = new JMenuItem("Electricity Billing Report");
        JMenuItem waterBillItem = new JMenuItem("Water Billing Report");
        JMenuItem gasBillItem = new JMenuItem("Gas Billing Report");
        JMenuItem userBillItem = new JMenuItem("User Billing Report");
        JMenuItem waterFeesQueryItem = new JMenuItem("Water Fees Query");  // 新增菜单项

        reportsMenu.add(electricityBillItem);
        reportsMenu.add(waterBillItem);
        reportsMenu.add(gasBillItem);
        reportsMenu.add(userBillItem);
        reportsMenu.add(waterFeesQueryItem);  // 添加到菜单中

        menuBar.add(reportsMenu);

        // 添加 Electricity Billing Report 的事件监听器
        electricityBillItem.addActionListener(e -> {
            ElectricityBillingReport electricityBillingReport = new ElectricityBillingReport();
            electricityBillingReport.setVisible(true);
        });

        // 添加 Water Billing Report 的事件监听器
        waterBillItem.addActionListener(e -> {
            WaterBillingReport waterBillingReport = new WaterBillingReport();
            waterBillingReport.setVisible(true);
        });

        // 添加 Gas Billing Report 的事件监听器
        gasBillItem.addActionListener(e -> {
            GasBillingReport gasBillingReport = new GasBillingReport();
            gasBillingReport.setVisible(true);
        });

        // 添加 User Billing Report 的事件监听器
        userBillItem.addActionListener(e -> {
            UserBillingReport userBillingReport = new UserBillingReport();
            userBillingReport.setVisible(true);
        });

        // 添加 Water Fees Query 的事件监听器
        waterFeesQueryItem.addActionListener(e -> {
            WaterBillingReport waterBillingReport = new WaterBillingReport();
            waterBillingReport.setVisible(true);
        });

        // 添加 Electricity Fees Query 的事件监听器
        JMenuItem electricityFeesQueryItem = new JMenuItem("Electricity Fees Query");
        electricityFeesQueryItem.addActionListener(e -> {
            ElectricityBillingReport electricityBillingReport = new ElectricityBillingReport();
            electricityBillingReport.setVisible(true);
        });
        reportsMenu.add(electricityFeesQueryItem);

        // 添加 Gas Fees Query 的事件监听器
        JMenuItem gasFeesQueryItem = new JMenuItem("Gas Fees Query");
        gasFeesQueryItem.addActionListener(e -> {
            GasBillingReport gasBillingReport = new GasBillingReport();
            gasBillingReport.setVisible(true);
        });
        reportsMenu.add(gasFeesQueryItem);

        // 添加 Total Fees Query 的事件监听器
        JMenuItem totalFeesQueryItem = new JMenuItem("Total Fees Query");
        totalFeesQueryItem.addActionListener(e -> {
            // 原功能已移除
        });
        reportsMenu.add(totalFeesQueryItem);

        // 添加 Exit menu item 到菜单栏
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        menuBar.add(exitItem);

        setJMenuBar(menuBar);

        // Add main content panel
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
