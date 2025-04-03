package gui;

import cn.hutool.db.Entity;
import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.Query;
import core.PropCore;
import core.StateData;
import dao.entity.UserInfo;
import db.MySql;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Collection;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginPanel() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        // 添加炫酷标题
        JLabel titleLabel = new JLabel("PropGuardian");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 123, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // 用户名输入框
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(usernameField, gbc);

        // 密码输入框
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(passwordField, gbc);

        // 登录按钮
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 使按钮横向填充空间
        add(loginButton, gbc);

        // 添加鼠标悬停效果到登录按钮
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(0, 97, 195)); // 鼠标悬停时改变背景颜色
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(0, 123, 255)); // 鼠标离开时恢复背景颜色
            }
        });

        // 登录按钮的动作监听器
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                MySql mySql = PropCore.INS.getMySql();

                try {
                    Collection<Entity> entities = mySql.use().find(Entity.create("userinfo").set("uname", username).set("paswrd", password));

                    if (entities.isEmpty()) {
                        JOptionPane.showMessageDialog(LoginPanel.this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    StateData.INS.setCurrentuser(entities.iterator().next().toBean(UserInfo.class));

                    JOptionPane.showMessageDialog(LoginPanel.this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 注册按钮
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 使按钮横向填充空间
        add(registerButton, gbc);

        // 添加鼠标悬停效果到注册按钮
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(0, 97, 195)); // 鼠标悬停时改变背景颜色
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(0, 123, 255)); // 鼠标离开时恢复背景颜色
            }
        });

        // 注册按钮的动作监听器
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 这里添加注册逻辑
                JOptionPane.showMessageDialog(LoginPanel.this, "Registration functionality is not yet implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 添加版权信息
        JLabel copyrightLabel = new JLabel("Powered by PropGuardian Team 2025");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(copyrightLabel, gbc);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new LoginPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}