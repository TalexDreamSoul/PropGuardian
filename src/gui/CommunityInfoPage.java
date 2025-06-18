package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import configuration.Config;
import core.PropCore;
import dao.entity.CommunityInfo;

import java.util.List;

public class CommunityInfoPage extends JFrame {
    private JTable table;
    private JTextField textFieldId;
    private JTextField textFieldName;
    private JTextField textFieldAddress;
    private JTextField textFieldArea;
    private JLabel totalLabel; // 统计标签
    private JLabel loadingLabel; // 加载动画标签
    private JButton confirmEditButton; // 确认修改按钮
    private boolean isTableEdited = false; // 表格是否被编辑
    private boolean isLoading = false; // 是否正在加载
    private JButton[] allButtons; // 所有按钮的引用

    private Db db;
    private volatile boolean dbConnectionValid = true; // 数据库连接状态

    public CommunityInfoPage() {
        initializeDatabase();
        
        // 启动数据库连接监控线程
        startDatabaseMonitor();

        setTitle("小区信息维护 - PropGuardian Property Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 增大窗口尺寸
        setSize(700, 480);
        setLocationRelativeTo(null);

        // 设置布局
        setLayout(new BorderLayout());

        // 创建加载动画标签
        loadingLabel = new JLabel("加载中...");
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        add(loadingLabel, BorderLayout.CENTER);

        // 初始化表格
        loadTableFromDatabase();

        // 创建统计面板
        JPanel statsPanel = new JPanel();
        totalLabel = new JLabel("小区总数: 0, 总面积: 0.0 平方米");
        totalLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        statsPanel.add(totalLabel);
        add(statsPanel, BorderLayout.NORTH);

        // 创建输入框面板
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("小区编号:"));
        textFieldId = new JTextField();
        inputPanel.add(textFieldId);
        inputPanel.add(new JLabel("小区名称:"));
        textFieldName = new JTextField();
        inputPanel.add(textFieldName);
        inputPanel.add(new JLabel("小区地址:"));
        textFieldAddress = new JTextField();
        inputPanel.add(textFieldAddress);
        inputPanel.add(new JLabel("占地面积:"));
        textFieldArea = new JTextField();
        inputPanel.add(textFieldArea);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5)); // 增加按钮间距
        
        JButton addButton = new JButton("添加");
        JButton editButton = new JButton("修改");
        JButton deleteButton = new JButton("删除");
        JButton resetButton = new JButton("重置");
        JButton queryButton = new JButton("查询");
        confirmEditButton = new JButton("确认修改");
        
        // 保存所有按钮引用
        allButtons = new JButton[]{addButton, editButton, deleteButton, resetButton, queryButton, confirmEditButton};

        // 设置按钮样式
        setButtonStyle(addButton);
        setButtonStyle(editButton);
        setButtonStyle(deleteButton);
        setButtonStyle(resetButton);
        setButtonStyle(queryButton);
        setConfirmButtonStyle(confirmEditButton); // 确认修改按钮使用特殊样式
        
        // 初始隐藏确认修改按钮
        confirmEditButton.setVisible(false);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCommunity();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCommunity();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCommunity();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryCommunity();
            }
        });

        confirmEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmTableEdit();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(queryButton);
        buttonPanel.add(confirmEditButton);

        // 将输入框和按钮面板添加到底部
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建版权信息面板
        JPanel bottomInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel copyrightLabel = new JLabel("Powered by TaGzxia © 2023-2024 PropGuardian Property Management System");
        copyrightLabel.setFont(new Font(Config.UIFonts.getFontName(), Font.PLAIN, 12));
        copyrightLabel.setForeground(Config.UIColors.TEXT_GRAY);
        bottomInfoPanel.add(copyrightLabel);

        // 组合底部面板
        JPanel mainBottomPanel = new JPanel(new BorderLayout());
        mainBottomPanel.add(bottomPanel, BorderLayout.CENTER);
        mainBottomPanel.add(bottomInfoPanel, BorderLayout.PAGE_END);

        add(mainBottomPanel, BorderLayout.SOUTH);

        // 添加样式设置方法调用
        setupStyles();
        // 更新统计信息
        updateStatistics();
    }

    // 新增样式设置方法
    private void setupStyles() {
        // 设置整体字体
        Font font = new Font(Config.UIFonts.getFontName(), Font.PLAIN, 12);
        setFont(font);

        // 设置标题
        setTitle("小区信息维护 - PropGuardian Property Management System");

        // 设置背景颜色
        getContentPane().setBackground(Config.UIColors.getBackgroundColor());

        // 设置标签样式
        totalLabel.setFont(new Font(Config.UIFonts.getFontName(), Font.BOLD, 14));
        totalLabel.setForeground(Config.UIColors.getTitleColor());

        // 设置加载动画样式
        loadingLabel.setFont(new Font(Config.UIFonts.getFontName(), Font.ITALIC, 14));
        loadingLabel.setForeground(Config.UIColors.TEXT_GRAY);

        // 设置表格样式
        if (table != null) {
            table.setFont(new Font(Config.UIFonts.getFontName(), Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setFont(new Font(Config.UIFonts.getFontName(), Font.BOLD, 13));
        }
    }

    // 按钮样式设置方法
    private void setButtonStyle(JButton button) {
        button.setBackground(Config.UIColors.getSecondaryColor()); // 蓝色主调
        button.setForeground(Config.UIColors.TEXT_WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16)); // 扁平化设计
        button.setFocusPainted(false);
        button.setFont(new Font(Config.UIFonts.getFontName(), Font.BOLD, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // 添加鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Config.UIColors.getSecondaryHoverColor());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Config.UIColors.getSecondaryColor());
            }
        });
    }

    // 新增输入验证方法
    private boolean validateInputs() {
        // 验证小区编号
        if (!textFieldId.getText().isEmpty()) {
            try {
                long id = Long.parseLong(textFieldId.getText());
                if (id <= 0) {
                    JOptionPane.showMessageDialog(this, "小区编号必须为正整数");
                    return false;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "小区编号必须为有效的数字");
                return false;
            }
        }
        
        // 验证小区名称（非空且长度限制）
        if (!textFieldName.getText().isEmpty() && textFieldName.getText().length() > 50) {
            JOptionPane.showMessageDialog(this, "小区名称长度不能超过50个字符");
            return false;
        }
        
        // 验证小区地址（非空且长度限制）
        if (!textFieldAddress.getText().isEmpty() && textFieldAddress.getText().length() > 100) {
            JOptionPane.showMessageDialog(this, "小区地址长度不能超过100个字符");
            return false;
        }
        
        // 验证占地面积
        if (!textFieldArea.getText().isEmpty()) {
            try {
                double area = Double.parseDouble(textFieldArea.getText());
                if (area <= 0) {
                    JOptionPane.showMessageDialog(this, "占地面积必须为正数");
                    return false;
                }
                if (area > 1000000) {
                    JOptionPane.showMessageDialog(this, "占地面积过大，请检查输入值");
                    return false;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "占地面积必须为有效的数字");
                return false;
            }
        }
        
        return true;
    }

    // 修改添加社区方法，增加验证
    private void addCommunity() {
        if (!validateInputs()) {
            return;
        }

        executeWithLoadingAndDbCheck(() -> {
            try {
                long id = Long.parseLong(textFieldId.getText());
                String name = textFieldName.getText().trim();
                String address = textFieldAddress.getText().trim();
                double area = Double.parseDouble(textFieldArea.getText());
                
                // 检查必填字段
                if (name.isEmpty() || address.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "小区名称和地址为必填项");
                    });
                    return;
                }
                
                this.db.insert(Entity.create("community_info")
                        .set("district_id", id)
                        .set("district_name", name)
                        .set("address", address)
                        .set("floor_space", area));

                SwingUtilities.invokeLater(() -> {
                    loadTableFromDatabase();
                    updateStatistics();
                    resetFields();
                    JOptionPane.showMessageDialog(this, "添加成功！");
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "添加失败: " + e.getMessage());
                });
            }
        }, "添加小区");
    }

    // 查询功能实现
    private void queryCommunity() {
        executeWithLoadingAndDbCheck(() -> {
            try {
                // 输入验证
                if (!validateInputs()) {
                    return;
                }
                
                String querySql = "SELECT * FROM community_info WHERE 1=1";
                
                // 根据输入框内容构建查询条件
                if (!textFieldId.getText().isEmpty()) {
                    querySql += " AND district_id = " + Long.parseLong(textFieldId.getText());
                }
                
                if (!textFieldName.getText().isEmpty()) {
                    querySql += " AND district_name LIKE '%" + textFieldName.getText() + "%'";
                }
                
                if (!textFieldAddress.getText().isEmpty()) {
                    querySql += " AND address LIKE '%" + textFieldAddress.getText() + "%'";
                }
                
                if (!textFieldArea.getText().isEmpty()) {
                    querySql += " AND floor_space = " + Double.parseDouble(textFieldArea.getText());
                }
                
                List<Entity> communityList = this.db.query(querySql);
                Object[][] data = new Object[communityList.size()][4];
                for (int i = 0; i < communityList.size(); i++) {
                    Entity community = communityList.get(i);
                    data[i][0] = community.getLong("district_id");
                    data[i][1] = community.getStr("district_name");
                    data[i][2] = community.getStr("address");
                    data[i][3] = community.getDouble("floor_space");
                }
                
                SwingUtilities.invokeLater(() -> {
                    String[] columnNames = {"小区编号", "小区名称", "小区地址", "占地面积"};
                    // 使用完全限定类名修复问题
                    table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
                    
                    // 更新统计信息
                    updateStatistics();
                    
                    JOptionPane.showMessageDialog(this, "查询完成，共找到 " + communityList.size() + " 条记录");
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "查询失败: " + e.getMessage());
                });
            }
        }, "查询小区");
    }

    // 加载数据库数据到表格
    private void loadTableFromDatabase() {
        try {
            // 检查数据库是否为空，如果为空则插入默认数据
            if (db.query("SELECT COUNT(*) FROM community_info").get(0).getLong("COUNT(*)") == 0) {
                insertDefaultData();
            }

            // 显示加载动画
            loadingLabel.setVisible(true);

            List<Entity> communityList = this.db.query("SELECT * FROM community_info");
            Object[][] data = new Object[communityList.size()][4];
            for (int i = 0; i < communityList.size(); i++) {
                Entity community = communityList.get(i);
                data[i][0] = community.getLong("district_id");
                data[i][1] = community.getStr("district_name");
                data[i][2] = community.getStr("address");
                data[i][3] = community.getDouble("floor_space");
            }
            String[] columnNames = {"小区编号", "小区名称", "小区地址", "占地面积"};
            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // 小区编号不可编辑，其他列可编辑
                    return column != 0;
                }
                
                @Override
                public void setValueAt(Object value, int row, int col) {
                    super.setValueAt(value, row, col);
                    // 检测到表格编辑，显示确认修改按钮
                    isTableEdited = true;
                    confirmEditButton.setVisible(true);
                    revalidate();
                    repaint();
                }
            };
            table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            // 隐藏加载动画
            loadingLabel.setVisible(false);

            // 添加 ListSelectionListener
            table.getSelectionModel().addListSelectionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    textFieldId.setText(table.getValueAt(selectedRow, 0).toString());
                    textFieldName.setText(table.getValueAt(selectedRow, 1).toString());
                    textFieldAddress.setText(table.getValueAt(selectedRow, 2).toString());
                    textFieldArea.setText(table.getValueAt(selectedRow, 3).toString());
                } else {
                    resetFields();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载数据失败: " + e.getMessage());
        }
    }

    // 插入默认数据
    private void insertDefaultData() {
        try {
            this.db.insert(Entity.create("community_info")
                    .set("district_id", 1L)
                    .set("district_name", "小区A")
                    .set("address", "地址A")
                    .set("floor_space", 1000.0));
            this.db.insert(Entity.create("community_info")
                    .set("district_id", 2L)
                    .set("district_name", "小区B")
                    .set("address", "地址B")
                    .set("floor_space", 1500.0));
            this.db.insert(Entity.create("community_info")
                    .set("district_id", 3L)
                    .set("district_name", "小区C")
                    .set("address", "地址C")
                    .set("floor_space", 2000.0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 修改小区信息
    private void editCommunity() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的小区！");
            return;
        }

        if (!validateInputs()) {
            return;
        }

        executeWithLoadingAndDbCheck(() -> {
            try {
                long id = Long.parseLong(textFieldId.getText());
                String name = textFieldName.getText();
                String address = textFieldAddress.getText();
                double area = Double.parseDouble(textFieldArea.getText());

                this.db.update(Entity.create("community_info")
                                .set("district_name", name)
                                .set("address", address)
                                .set("floor_space", area),
                        Entity.create().set("district_id", id));

                SwingUtilities.invokeLater(() -> {
                    loadTableFromDatabase();
                    updateStatistics();
                    resetFields();
                    JOptionPane.showMessageDialog(this, "修改成功！");
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "修改失败: " + e.getMessage());
                });
            }
        }, "修改小区");
    }

    // 删除小区信息
    private void deleteCommunity() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的行！");
            return;
        }

        int result = JOptionPane.showConfirmDialog(this, "确认要删除选中的小区信息吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            executeWithLoadingAndDbCheck(() -> {
                try {
                    long id = (Long) table.getValueAt(selectedRow, 0);
                    this.db.del(new CommunityInfo().getEntity().set("district_id", id));

                    SwingUtilities.invokeLater(() -> {
                        loadTableFromDatabase();
                        updateStatistics();
                        resetFields();
                        JOptionPane.showMessageDialog(this, "删除成功！");
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "删除失败: " + e.getMessage());
                    });
                }
            }, "删除小区");
        }
    }

    // 重置输入框
    private void resetFields() {
        textFieldId.setText("");
        textFieldName.setText("");
        textFieldAddress.setText("");
        textFieldArea.setText("");
        // 添加刷新表格的功能
        loadTableFromDatabase();
        updateStatistics();
    }

    // 更新统计信息
    private void updateStatistics() {
        try {
            int rowCount = table.getRowCount();
            double totalArea = 0.0;
            for (int i = 0; i < rowCount; i++) {
                totalArea += Double.parseDouble(table.getValueAt(i, 3).toString());
            }
            totalLabel.setText("小区总数: " + rowCount + ", 总面积: " + totalArea + " 平方米");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 确认表格编辑方法
    private void confirmTableEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的行！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 获取表格中的数据
        Object idObj = table.getValueAt(selectedRow, 0);
        Object nameObj = table.getValueAt(selectedRow, 1);
        Object addressObj = table.getValueAt(selectedRow, 2);
        Object areaObj = table.getValueAt(selectedRow, 3);

        // 验证数据
        if (nameObj == null || nameObj.toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "小区名称不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (addressObj == null || addressObj.toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "小区地址不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double area = Double.parseDouble(areaObj.toString());
            if (area <= 0) {
                JOptionPane.showMessageDialog(this, "占地面积必须为正数！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "占地面积必须为有效数字！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 确认对话框
        String message = String.format("确认要修改以下信息吗？\n\n" +
                "小区编号: %s\n" +
                "小区名称: %s\n" +
                "小区地址: %s\n" +
                "占地面积: %s 平方米",
                idObj.toString(),
                nameObj.toString().trim(),
                addressObj.toString().trim(),
                areaObj.toString());

        int result = JOptionPane.showConfirmDialog(this, message, "确认修改", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            executeWithLoadingAndDbCheck(() -> {
                try {
                    long id = Long.parseLong(idObj.toString());
                    String name = nameObj.toString().trim();
                    String address = addressObj.toString().trim();
                    double area = Double.parseDouble(areaObj.toString());

                    // 更新数据库
                    this.db.update(Entity.create("community_info")
                                    .set("district_name", name)
                                    .set("address", address)
                                    .set("floor_space", area),
                            Entity.create().set("district_id", id));

                    SwingUtilities.invokeLater(() -> {
                        // 刷新表格和统计信息
                        loadTableFromDatabase();
                        updateStatistics();
                        
                        // 更新输入框显示
                        textFieldId.setText(String.valueOf(id));
                        textFieldName.setText(name);
                        textFieldAddress.setText(address);
                        textFieldArea.setText(String.valueOf(area));

                        JOptionPane.showMessageDialog(this, "修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "修改失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    });
                }
            }, "确认修改");
        }
    }

    // 初始化数据库连接
    private void initializeDatabase() {
        try {
            Db use = PropCore.INS.getMySql().use();
            this.db = use;
            dbConnectionValid = true;
        } catch (Exception e) {
            dbConnectionValid = false;
            JOptionPane.showMessageDialog(this, "数据库连接失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 启动数据库连接监控线程
    private void startDatabaseMonitor() {
        Thread monitorThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000); // 每30秒检查一次
                    checkDatabaseConnection();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.start();
    }
    
    // 检查数据库连接状态
    private void checkDatabaseConnection() {
        try {
            // 执行简单查询测试连接
            db.query("SELECT 1");
            if (!dbConnectionValid) {
                dbConnectionValid = true;
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "数据库连接已恢复", "提示", JOptionPane.INFORMATION_MESSAGE);
                });
            }
        } catch (Exception e) {
            if (dbConnectionValid) {
                dbConnectionValid = false;
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "数据库连接丢失，正在尝试重连...", "警告", JOptionPane.WARNING_MESSAGE);
                });
            }
            // 尝试重新连接
            restartDatabaseConnection();
        }
    }
    
    // 重启数据库连接
    private void restartDatabaseConnection() {
        try {
            Thread.sleep(5000); // 等待5秒后重试
            initializeDatabase();
            if (dbConnectionValid) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "数据库重连成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                });
            }
        } catch (Exception e) {
            // 重连失败，继续监控
        }
    }
    
    // 设置确认修改按钮的特殊样式
    private void setConfirmButtonStyle(JButton button) {
        button.setFont(new Font(Config.UIFonts.getFontName(), Font.BOLD, 14));
        button.setBackground(new Color(255, 140, 0)); // 橙色背景
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
        
        // 鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 165, 0)); // 悬停时更亮的橙色
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 140, 0)); // 恢复原色
            }
        });
    }
    
    // 设置loading状态
    private void setLoadingState(boolean loading) {
        isLoading = loading;
        loadingLabel.setVisible(loading);
        
        // 禁用/启用所有按钮
        for (JButton button : allButtons) {
            if (button != null) {
                button.setEnabled(!loading);
            }
        }
        
        // 禁用/启用输入框
        textFieldId.setEnabled(!loading);
        textFieldName.setEnabled(!loading);
        textFieldAddress.setEnabled(!loading);
        textFieldArea.setEnabled(!loading);
        
        // 禁用/启用表格
        if (table != null) {
            table.setEnabled(!loading);
        }
        
        repaint();
    }
    
    // 执行数据库操作的安全包装方法
    private void executeWithLoadingAndDbCheck(Runnable operation, String operationName) {
        if (!dbConnectionValid) {
            JOptionPane.showMessageDialog(this, "数据库连接不可用，请稍后重试", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> setLoadingState(true));
                Thread.sleep(500); // 模拟加载时间，让用户看到loading状态
                operation.run();
                return null;
            }
            
            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    setLoadingState(false);
                    // 操作完成后隐藏确认修改按钮
                    if (isTableEdited) {
                        isTableEdited = false;
                        confirmEditButton.setVisible(false);
                        revalidate();
                        repaint();
                    }
                });
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CommunityInfoPage page = new CommunityInfoPage();
            page.setVisible(true);
        });
    }
}