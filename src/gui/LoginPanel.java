package gui;

import cn.hutool.db.Entity;
import configuration.Config;
import configuration.Env;
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
        // 初始化环境配置
        Env.getInstance().loadEnvProperties();
        
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(40, 40, 40, 40));
        setBackground(Config.UIColors.BACKGROUND_WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("PropGuardian");
        titleLabel.setFont(new Font(Config.UIFonts.getEnglishFontName(), Font.BOLD, 24));
        titleLabel.setForeground(Config.UIColors.getPrimaryColor());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);


        usernameField = new JTextField(20);
        usernameField.setFont(new Font(Config.UIFonts.getEnglishFontName(), Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(usernameField, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font(Config.UIFonts.getEnglishFontName(), Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(passwordField, gbc);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font(Config.UIFonts.getEnglishFontName(), Font.BOLD, 16));
        loginButton.setBackground(Config.UIColors.getPrimaryColor());
        loginButton.setForeground(Config.UIColors.TEXT_WHITE);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(loginButton, gbc);

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(Config.UIColors.getPrimaryHoverColor()); // 鼠标悬停时改变背景颜色
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(Config.UIColors.getPrimaryColor()); // 鼠标离开时恢复背景颜色
            }
        });

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

                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginPanel.this);
                    frame.dispose();
                    new Index().setVisible(true);

                    JOptionPane.showMessageDialog(LoginPanel.this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton = new JButton("Register");
        registerButton.setFont(new Font(Config.UIFonts.getEnglishFontName(), Font.BOLD, 16));
        registerButton.setBackground(Config.UIColors.getPrimaryColor());
        registerButton.setForeground(Config.UIColors.TEXT_WHITE);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(registerButton, gbc);

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(Config.UIColors.getPrimaryHoverColor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(Config.UIColors.getPrimaryColor());
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(LoginPanel.this, "Registration functionality is not yet implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JLabel copyrightLabel = new JLabel("Powered by PropGuardian Team 2025");
        copyrightLabel.setFont(new Font(Config.UIFonts.getEnglishFontName(), Font.PLAIN, 12));
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