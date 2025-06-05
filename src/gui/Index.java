package gui;

import javax.swing.*;
import java.awt.*;

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
        JMenuItem userInfoItem = new JMenuItem("User Info Maintenance");
        JMenuItem modifyRateItem = new JMenuItem("Modify Charge Rate");
        JMenuItem queryRateItem = new JMenuItem("Query Charge Rate");
        
        // 新增：Online Repair Service 和 Notifications 子菜单项
        JMenuItem onlineRepairItem = new JMenuItem("Online Repair Service");
        onlineRepairItem.addActionListener(e -> {
            OnlineRepairService repairService = new OnlineRepairService();
            repairService.setVisible(true);
        });
        
        JMenuItem notificationItem = new JMenuItem("Notifications");
        notificationItem.addActionListener(e -> {
            NotificationPanel notificationPanel = new NotificationPanel();
            notificationPanel.setVisible(true);
        });
        
        // 添加原有菜单项
        basicInfoMenu.add(communityInfoItem);
        basicInfoMenu.add(buildingInfoItem);
        basicInfoMenu.add(houseInfoItem);
        basicInfoMenu.add(userInfoItem);
        basicInfoMenu.add(modifyRateItem);
        basicInfoMenu.add(queryRateItem);
        // 按顺序继续添加新菜单项，不使用分隔线
        basicInfoMenu.add(onlineRepairItem);
        basicInfoMenu.add(notificationItem);
        menuBar.add(basicInfoMenu);

        // Add action listener to "Community Info Maintenance" menu item
        communityInfoItem.addActionListener(e -> {
            CommunityInfoPage communityInfoPage = new CommunityInfoPage();
            communityInfoPage.setVisible(true);
        });
        // Add action listener to "House Info Maintenance" menu item
        houseInfoItem.addActionListener(e -> {
            OwnerInfoPage houseInfoPage = new OwnerInfoPage();
            houseInfoPage.setVisible(true);
        });
        // Add action listener to "User Info Maintenance" menu item
        userInfoItem.addActionListener(e -> {
            UserInfoPage userInfoPage = new UserInfoPage();
            userInfoPage.setVisible(true);
        });
        // Add action listener to "Modify Charge Rate" menu item
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

        ownerIndexItem.addActionListener(e -> {
            new SelectCommunityAndBuilding().setVisible(true);
        });

        // Add fee report menu and its sub-menu items
        JMenu reportsMenu = new JMenu("Fee Reports");
        JMenuItem electricityBillItem = new JMenuItem("Electricity Billing Report");
        JMenuItem waterBillItem = new JMenuItem("Water Billing Report");
        JMenuItem gasBillItem = new JMenuItem("Gas Billing Report");
        JMenuItem userBillItem = new JMenuItem("User Billing Report");
        JMenuItem propertyBillItem = new JMenuItem("Property Billing Report");

        reportsMenu.add(electricityBillItem);
        reportsMenu.add(waterBillItem);
        reportsMenu.add(gasBillItem);
        reportsMenu.add(userBillItem);
        reportsMenu.add(propertyBillItem);

        menuBar.add(reportsMenu);


        // 添加 User Billing Report 的事件监听器
        userBillItem.addActionListener(e -> {
            UserBillingReport userBillingReport = new UserBillingReport();
            userBillingReport.setVisible(true);
        });
        propertyBillItem.addActionListener(e -> {
            new PropertyBillingReport().setVisible(true);
        });
        // 添加 Exit menu item 到菜单栏

        // Add user information management menu and its sub-menu items
        JMenu userInfoMenu = new JMenu("User Information Management");
        JMenuItem manageUserInfoItem = new JMenuItem("Manage User Information");

        userInfoMenu.add(manageUserInfoItem);
        menuBar.add(userInfoMenu);

        // Add action listener to "Manage User Information" menu item
        manageUserInfoItem.addActionListener(e -> {
            UserInfoPage userInfoPage = new UserInfoPage();
            userInfoPage.setVisible(true);
        });

        // Add Exit menu item 到菜单栏
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
