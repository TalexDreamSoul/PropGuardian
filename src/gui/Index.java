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

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu manageMenu = new JMenu("Manage");
        JMenuItem residentsItem = new JMenuItem("Residents");
        JMenuItem propertiesItem = new JMenuItem("Properties");
        manageMenu.add(residentsItem);
        manageMenu.add(propertiesItem);
        menuBar.add(manageMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

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