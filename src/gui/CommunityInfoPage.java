package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import configuration.Config;
import core.PropCore;
import dao.entity.CommunityInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 小区信息维护页面
 * 提供小区信息的增删改查功能，包括数据验证、统计信息显示等
 * 
 * @author PropGuardian Team
 * @version 1.0
 * @since 2025
 */
public class CommunityInfoPage extends JFrame {
    private JTable table;
    private JScrollPane scrollPane; // 添加滚动面板引用
    private JPanel mainContentPanel; // 主内容面板，用于切换显示
    private JTextField textFieldId;
    private JTextField textFieldName;
    private JTextField textFieldAddress;
    private JTextField textFieldArea;
    private JLabel totalLabel;
    private JLabel loadingLabel;
    private JButton confirmEditButton;
    private boolean isTableEdited = false;
    private boolean isLoading = false;
    private JButton[] allButtons;
    private DefaultTableModel tableModel; // 添加表格模型引用

    /** 数据库连接对象 */
    private Db db;
    /** 数据库连接状态标志 */
    private volatile boolean dbConnectionValid = true;

    /**
     * 构造函数，初始化小区信息维护页面
     */
    public CommunityInfoPage() {
        initializeDatabase();
        startDatabaseMonitor();

        setTitle("小区信息维护 - PropGuardian Property Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 创建主内容面板，用于切换loading和表格显示
        mainContentPanel = new JPanel(new BorderLayout());
        
        // 创建加载动画标签
        loadingLabel = new JLabel("加载中...", JLabel.CENTER);
        loadingLabel.setFont(new Font("微软雅黑", Font.BOLD, 20)); // 增大字体
        loadingLabel.setForeground(new Color(0, 123, 255)); // 使用更明显的蓝色
        loadingLabel.setOpaque(true);
        loadingLabel.setBackground(Color.WHITE);
        
        // 初始化表格结构（只创建一次）
        initializeTableStructure();
        
        // 先显示loading
        showLoading(true);
        add(mainContentPanel, BorderLayout.CENTER);

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
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton addButton = new JButton("添加");
        JButton editButton = new JButton("修改");
        JButton deleteButton = new JButton("删除");
        JButton resetButton = new JButton("重置");
        JButton queryButton = new JButton("查询");
        confirmEditButton = new JButton("确认修改");

        // 保存所有按钮引用
        allButtons = new JButton[] { addButton, editButton, deleteButton, resetButton, queryButton, confirmEditButton };

        // 设置按钮样式
        setButtonStyle(addButton);
        setButtonStyle(editButton);
        setButtonStyle(deleteButton);
        setButtonStyle(resetButton);
        setButtonStyle(queryButton);
        setConfirmButtonStyle(confirmEditButton);

        // 初始隐藏确认修改按钮
        confirmEditButton.setVisible(false);

        addButton.addActionListener(e -> addCommunity());
        editButton.addActionListener(e -> editCommunity());
        deleteButton.addActionListener(e -> deleteCommunity());
        resetButton.addActionListener(e -> resetFields());
        queryButton.addActionListener(e -> queryCommunity());
        confirmEditButton.addActionListener(e -> confirmTableEdit());

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

        setupStyles();
        
        // 异步加载数据
        SwingUtilities.invokeLater(() -> loadTableData());
    }

    /**
     * 初始化表格结构（只创建一次）
     */
    private void initializeTableStructure() {
        String[] columnNames = { "小区编号", "小区名称", "小区地址", "占地面积" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // 小区编号不可编辑
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                super.setValueAt(value, row, col);
                isTableEdited = true;
                SwingUtilities.invokeLater(() -> {
                    confirmEditButton.setVisible(true);
                    revalidate();
                    repaint();
                });
            }
        };
        
        table = new JTable(tableModel);
        
        // 为占地面积列设置自定义渲染器
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

            @Override
            public void setValue(Object value) {
                if (value instanceof Number) {
                    setText(formatter.format(((Number) value).doubleValue()));
                } else {
                    setText(value != null ? value.toString() : "");
                }
            }
        });

        // 添加选择监听器
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    SwingUtilities.invokeLater(() -> {
                        textFieldId.setText(table.getValueAt(selectedRow, 0).toString());
                        textFieldName.setText(table.getValueAt(selectedRow, 1).toString());
                        textFieldAddress.setText(table.getValueAt(selectedRow, 2).toString());
                        textFieldArea.setText(table.getValueAt(selectedRow, 3).toString());
                    });
                }
            }
        });
        
        scrollPane = new JScrollPane(table);
    }

    /**
     * 控制加载动画和表格的显示
     */
    private void showLoading(boolean loading) {
        SwingUtilities.invokeLater(() -> {
            mainContentPanel.removeAll();
            if (loading) {
                mainContentPanel.add(loadingLabel, BorderLayout.CENTER);
            } else {
                mainContentPanel.add(scrollPane, BorderLayout.CENTER);
            }
            mainContentPanel.revalidate();
            mainContentPanel.repaint();
        });
    }

    /**
     * 加载表格数据（只更新数据，不重新创建表格）
     */
    private void loadTableData() {
        executeWithLoadingAndDbCheck(() -> {
            try {
                // 检查数据库是否为空
                List<Entity> countResult = db.query("SELECT COUNT(*) as count FROM community_info");
                if (countResult.get(0).getLong("count") == 0) {
                    insertDefaultData();
                }

                // 查询所有数据
                List<Entity> communityList = db.findAll("community_info");
                
                SwingUtilities.invokeLater(() -> {
                    // 清空现有数据
                    tableModel.setRowCount(0);
                    
                    // 添加新数据
                    for (Entity community : communityList) {
                        Object[] rowData = {
                            community.getLong("district_id"),
                            community.getStr("district_name"),
                            community.getStr("address"),
                            community.getDouble("floor_space")
                        };
                        tableModel.addRow(rowData);
                    }
                    
                    updateStatistics();
                    showLoading(false); // 数据加载完成后显示表格
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    showLoading(false);
                    JOptionPane.showMessageDialog(this, "加载数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        }, "加载数据");
    }

    /**
     * 重置输入框内容（优化版本）
     */
    private void resetFields() {
        SwingUtilities.invokeLater(() -> {
            textFieldId.setText("");
            textFieldName.setText("");
            textFieldAddress.setText("");
            textFieldArea.setText("");
            
            // 清除表格选择
            table.clearSelection();
            
            // 隐藏确认修改按钮
            if (isTableEdited) {
                isTableEdited = false;
                confirmEditButton.setVisible(false);
                revalidate();
                repaint();
            }
        });
    }

    /**
     * 设置加载状态
     */
    private void setLoadingState(boolean loading) {
        setLoadingState(loading, "加载中...");
    }

    private void setLoadingState(boolean loading, String operationText) {
        SwingUtilities.invokeLater(() -> {
            isLoading = loading;
            loadingLabel.setText(operationText);
            
            // 显示/隐藏加载动画
            if (loading) {
                showLoading(true);
            }
            
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
        });
    }

    /**
     * 执行数据库操作的安全包装方法
     */
    private void executeWithLoadingAndDbCheck(Runnable operation, String operationName) {
        if (!dbConnectionValid) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "数据库连接不可用，请稍后重试", "错误", JOptionPane.ERROR_MESSAGE);
            });
            return;
        }

        setLoadingState(true, operationName + "中...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(300); // 短暂延迟让用户看到loading状态
                operation.run();
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    setLoadingState(false);
                    if (!operationName.equals("加载数据")) {
                        showLoading(false); // 确保操作完成后显示表格
                    }
                });
            }
        };

        worker.execute();
    }

    /**
     * 初始化数据库连接
     */
    /**
     * 初始化数据库连接
     */
    private void initializeDatabase() {
        try {
            // 修复：使用正确的数据库连接方式
            this.db = PropCore.INS.getMySql().use();
            dbConnectionValid = true;
        } catch (Exception e) {
            e.printStackTrace();
            dbConnectionValid = false;
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "数据库连接失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            });
        };
    }

    /**
     * 启动数据库连接监控线程
     */
    private void startDatabaseMonitor() {
        Thread monitorThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000); // 每30秒检查一次
                    if (db != null) {
                        // 简单的连接测试
                        db.query("SELECT 1");
                        dbConnectionValid = true;
                    } else {
                        dbConnectionValid = false;
                    }
                } catch (Exception e) {
                    dbConnectionValid = false;
                    // 可以选择记录日志而不是打印到控制台
                    System.err.println("数据库连接检查失败: " + e.getMessage());
                }
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    /**
     * 插入默认测试数据
     */
    private void insertDefaultData() {
        try {
            // 检查表是否存在数据
            List<Entity> existing = db.query("SELECT COUNT(*) as count FROM community_info");
            if (existing.get(0).getLong("count") > 0) {
                return; // 如果已有数据，不插入默认数据
            }
            
            // 插入一些默认的小区数据
            db.execute("INSERT INTO community_info (district_id, district_name, address, floor_space) VALUES (?, ?, ?, ?)",
                    1, "阳光花园", "北京市朝阳区阳光街1号", 15000.50);
            db.execute("INSERT INTO community_info (district_id, district_name, address, floor_space) VALUES (?, ?, ?, ?)",
                    2, "绿城小区", "上海市浦东新区绿城路88号", 22000.75);
            db.execute("INSERT INTO community_info (district_id, district_name, address, floor_space) VALUES (?, ?, ?, ?)",
                    3, "华府庭院", "广州市天河区华府大道168号", 18500.25);
                
            System.out.println("默认数据插入成功");
        } catch (Exception e) {
            System.err.println("插入默认数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 设置按钮样式
     */
    private void setButtonStyle(JButton button) {
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(80, 30));
        
        // 添加鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    /**
     * 设置确认按钮样式
     */
    private void setConfirmButtonStyle(JButton button) {
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setBackground(new Color(255, 140, 0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 30));
        
        // 添加鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 165, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 140, 0));
            }
        });
    }

    /**
     * 设置整体样式
     */
    private void setupStyles() {
        // 设置整体字体
        Font defaultFont = new Font("微软雅黑", Font.PLAIN, 12);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        
        // 设置表格样式
        if (table != null) {
            table.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
            table.setRowHeight(25);
            table.setGridColor(new Color(230, 230, 230));
            table.setSelectionBackground(new Color(184, 207, 229));
        }
    }

    /**
     * 添加小区信息
     */
    private void addCommunity() {
        executeWithLoadingAndDbCheck(() -> {
            try {
                String id = textFieldId.getText().trim();
                String name = textFieldName.getText().trim();
                String address = textFieldAddress.getText().trim();
                String area = textFieldArea.getText().trim();
                
                if (id.isEmpty() || name.isEmpty() || address.isEmpty() || area.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "请填写完整信息！", "提示", JOptionPane.WARNING_MESSAGE);
                    });
                    return;
                }
                
                // 检查ID是否已存在
                List<Entity> existing = db.query("SELECT * FROM community_info WHERE district_id = ?", Long.parseLong(id));
                if (!existing.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "小区编号已存在！", "错误", JOptionPane.ERROR_MESSAGE);
                    });
                    return;
                }
                
                db.execute("INSERT INTO community_info (district_id, district_name, address, floor_space) VALUES (?, ?, ?, ?)",
                        Long.parseLong(id), name, address, Double.parseDouble(area));
                
                SwingUtilities.invokeLater(() -> {
                    loadTableData();
                    resetFields();
                    JOptionPane.showMessageDialog(this, "添加成功！");
                });
            } catch (NumberFormatException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "请输入有效的数字！", "错误", JOptionPane.ERROR_MESSAGE);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "添加失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        }, "添加小区");
    }

    /**
     * 修改小区信息
     */
    private void editCommunity() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的小区！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        executeWithLoadingAndDbCheck(() -> {
            try {
                String id = textFieldId.getText().trim();
                String name = textFieldName.getText().trim();
                String address = textFieldAddress.getText().trim();
                String area = textFieldArea.getText().trim();
                
                if (name.isEmpty() || address.isEmpty() || area.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "请填写完整信息！", "提示", JOptionPane.WARNING_MESSAGE);
                    });
                    return;
                }
                
                db.execute("UPDATE community_info SET district_name = ?, address = ?, floor_space = ? WHERE district_id = ?",
                        name, address, Double.parseDouble(area), Long.parseLong(id));
                
                SwingUtilities.invokeLater(() -> {
                    loadTableData();
                    JOptionPane.showMessageDialog(this, "修改成功！");
                });
            } catch (NumberFormatException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "请输入有效的数字！", "错误", JOptionPane.ERROR_MESSAGE);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "修改失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        }, "修改小区");
    }

    /**
     * 删除小区信息
     */
    private void deleteCommunity() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的小区！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, "确定要删除选中的小区吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            executeWithLoadingAndDbCheck(() -> {
                try {
                    String id = table.getValueAt(selectedRow, 0).toString();
                    db.execute("DELETE FROM community_info WHERE district_id = ?", Long.parseLong(id));
                    
                    SwingUtilities.invokeLater(() -> {
                        loadTableData();
                        resetFields();
                        JOptionPane.showMessageDialog(this, "删除成功！");
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "删除失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    });
                    e.printStackTrace();
                }
            }, "删除小区");
        }
    }

    /**
     * 查询小区信息
     */
    private void queryCommunity() {
        executeWithLoadingAndDbCheck(() -> {
            try {
                String name = textFieldName.getText().trim();
                String address = textFieldAddress.getText().trim();
                
                StringBuilder sql = new StringBuilder("SELECT * FROM community_info WHERE 1=1");
                List<Object> params = new ArrayList<>();
                
                if (!name.isEmpty()) {
                    sql.append(" AND district_name LIKE ?");
                    params.add("%" + name + "%");
                }
                
                if (!address.isEmpty()) {
                    sql.append(" AND address LIKE ?");
                    params.add("%" + address + "%");
                }
                
                List<Entity> communityList = db.query(sql.toString(), params.toArray());
                
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    for (Entity community : communityList) {
                        Object[] rowData = {
                            community.getLong("district_id"),
                            community.getStr("district_name"),
                            community.getStr("address"),
                            community.getDouble("floor_space")
                        };
                        tableModel.addRow(rowData);
                    }
                    updateStatistics();
                    JOptionPane.showMessageDialog(this, "查询完成，共找到 " + communityList.size() + " 条记录");
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "查询失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        }, "查询小区");
    }

    /**
     * 确认表格编辑
     */
    private void confirmTableEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的行！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        executeWithLoadingAndDbCheck(() -> {
            try {
                Object id = table.getValueAt(selectedRow, 0);
                Object name = table.getValueAt(selectedRow, 1);
                Object address = table.getValueAt(selectedRow, 2);
                Object area = table.getValueAt(selectedRow, 3);
                
                db.execute("UPDATE community_info SET district_name = ?, address = ?, floor_space = ? WHERE district_id = ?",
                        name.toString(), address.toString(), Double.parseDouble(area.toString()), Long.parseLong(id.toString()));
                
                SwingUtilities.invokeLater(() -> {
                    isTableEdited = false;
                    confirmEditButton.setVisible(false);
                    updateStatistics();
                    JOptionPane.showMessageDialog(this, "修改成功！");
                    revalidate();
                    repaint();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "修改失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        }, "确认修改");
    }

    /**
     * 更新统计信息
     */
    private void updateStatistics() {
        try {
            int rowCount = table.getRowCount();
            double totalArea = 0.0;
            for (int i = 0; i < rowCount; i++) {
                Object value = table.getValueAt(i, 3);
                if (value != null) {
                    totalArea += Double.parseDouble(value.toString());
                }
            }
            DecimalFormat formatter = new DecimalFormat("#,##0.00");
            totalLabel.setText("小区总数: " + rowCount + ", 总面积: " + formatter.format(totalArea) + " 平方米");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}