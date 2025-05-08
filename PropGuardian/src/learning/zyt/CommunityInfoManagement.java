
public class CommunityInfoManagement extends JFrame {
    private JTextField idField, nameField, addressField, areaField;
    private JButton searchButton, addButton, updateButton, deleteButton, resetButton;
    private JTextArea resultArea;
    private Map<String, CommunityInfo> communityMap;
    private JTable table; // 新增：用于显示小区信息列表

    public CommunityInfoManagement() {
        setTitle("小区信息维护界面");
        setSize(800, 600); // 调整窗口大小
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 初始化组件
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("小区编号:"));
        idField = new JTextField();
        inputPanel.add(idField);
        inputPanel.add(new JLabel("小区名称:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("小区地址:"));
        addressField = new JTextField();
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("占地面积:"));
        areaField = new JTextField();
        inputPanel.add(areaField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        searchButton = new JButton("查询");
        addButton = new JButton("添加");
        updateButton = new JButton("修改");
        deleteButton = new JButton("删除");
        resetButton = new JButton("重置");
        buttonPanel.add(searchButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(resetButton);

        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);

        // 新增：初始化表格
        String[] columnNames = {"小区编号", "小区名称", "小区地址", "占地面积"};
        Object[][] data = {};
        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // 添加组件到窗口
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH); // 将表格放置在南部区域

        // 初始化数据存储
        communityMap = new HashMap<>();

        // 添加事件监听器
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                CommunityInfo community = communityMap.get(id);
                if (community != null) {
                    displayCommunityInfo(community);
                } else {
                    resultArea.setText("未找到该小区信息");
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String name = nameField.getText();
                String address = addressField.getText();
                String area = areaField.getText();

                if (id.isEmpty() || name.isEmpty() || address.isEmpty() || area.isEmpty()) {
                    resultArea.setText("请填写所有字段");
                    return;
                }

                CommunityInfo community = new CommunityInfo(id, name, address, area);
                communityMap.put(id, community);
                resultArea.setText("小区信息已添加");
                clearFields();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                CommunityInfo community = communityMap.get(id);
                if (community != null) {
                    String name = nameField.getText();
                    String address = addressField.getText();
                    String area = areaField.getText();

                    if (!name.isEmpty()) community.setName(name);
                    if (!address.isEmpty()) community.setAddress(address);
                    if (!area.isEmpty()) community.setArea(area);

                    resultArea.setText("小区信息已更新");
                    clearFields();
                } else {
                    resultArea.setText("未找到该小区信息");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                if (communityMap.remove(id) != null) {
                    resultArea.setText("小区信息已删除");
                    clearFields();
                } else {
                    resultArea.setText("未找到该小区信息");
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
                resultArea.setText("");
            }
        });
    }

    private void displayCommunityInfo(CommunityInfo community) {
        resultArea.setText("小区编号: " + community.getId() + "\n" +
                           "小区名称: " + community.getName() + "\n" +
                           "小区地址: " + community.getAddress() + "\n" +
                           "占地面积: " + community.getArea());
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        addressField.setText("");
        areaField.setText("");
    }
}

class CommunityInfo {
    private String id;
    private String name;
    private String address;
    private String area;

    public CommunityInfo(String id, String name, String address, String area) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}