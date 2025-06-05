
// 根据楼宇ID加载房间号
private void loadRoomIds(String buildingId) {
    try {
        // 修改此处：查询 meter_reading 表并获取 room_number 字段
        List<Entity> rooms = db.query("SELECT DISTINCT room_number FROM meter_reading WHERE building_id = ?", buildingId);
        String[] roomIds = rooms.stream().map(entity -> entity.getStr("room_number")).toArray(String[]::new);
        ((JComboBox<String>) roomIdField).setModel(new DefaultComboBoxModel<>(roomIds)); // 修改此处
    } catch (Exception ex) {
        // 优化错误处理：提供更详细的错误信息
        JOptionPane.showMessageDialog(this, "加载房间号失败: " + ex.getMessage() + "\n请检查数据库表是否存在或字段是否正确", "错误", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
        roomIdField.setModel(new DefaultComboBoxModel<>(new String[]{"加载失败"})); // 添加默认值提示
    }
}
