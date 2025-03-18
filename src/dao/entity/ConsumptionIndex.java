package dao.entity;

public class ConsumptionIndex {
    private long districtId; // 小区ID
    private long buildingId; // 楼宇ID
    private long roomId;     // 业主ID
    private int date;        // 日期
    private double waterReading; // 用水量
    private double elecReading;  // 用电量
    private double gasReading;   // 用气量

    // 构造函数
    public ConsumptionIndex(long districtId, long buildingId, long roomId, int date, double waterReading, double elecReading, double gasReading) {
        this.districtId = districtId;
        this.buildingId = buildingId;
        this.roomId = roomId;
        this.date = date;
        this.waterReading = waterReading;
        this.elecReading = elecReading;
        this.gasReading = gasReading;
    }

    // Getter 和 Setter 方法
    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public double getWaterReading() {
        return waterReading;
    }

    public void setWaterReading(double waterReading) {
        this.waterReading = waterReading;
    }

    public double getElecReading() {
        return elecReading;
    }

    public void setElecReading(double elecReading) {
        this.elecReading = elecReading;
    }

    public double getGasReading() {
        return gasReading;
    }

    public void setGasReading(double gasReading) {
        this.gasReading = gasReading;
    }

    @Override
    public String toString() {
        return "ConsumptionIndex{" +
                "districtId=" + districtId +
                ", buildingId=" + buildingId +
                ", roomId=" + roomId +
                ", date=" + date +
                ", waterReading=" + waterReading +
                ", elecReading=" + elecReading +
                ", gasReading=" + gasReading +
                '}';
    }
}
