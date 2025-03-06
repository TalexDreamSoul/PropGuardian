package dao.entity;

public class Building {
    private short districtId; // 小区ID
    private short buildingId; // 楼宇ID
    private short totalStorey; // 楼层数
    private double totalArea; // 总面积
    private double height; // 楼高
    private short type; // 类型
    private String status; // 状态

    // 构造函数
    public Building(short districtId, short buildingId, short totalStorey, double totalArea, double height, short type, String status) {
        this.districtId = districtId;
        this.buildingId = buildingId;
        this.totalStorey = totalStorey;
        this.totalArea = totalArea;
        this.height = height;
        this.type = type;
        this.status = status;
    }

    // districtId的getter和setter
    public short getDistrictId() {
        return districtId;
    }

    public void setDistrictId(short districtId) {
        this.districtId = districtId;
    }

    // buildingId的getter和setter
    public short getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(short buildingId) {
        this.buildingId = buildingId;
    }

    // totalStorey的getter和setter
    public short getTotalStorey() {
        return totalStorey;
    }

    public void setTotalStorey(short totalStorey) {
        this.totalStorey = totalStorey;
    }

    // totalArea的getter和setter
    public double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(double totalArea) {
        this.totalArea = totalArea;
    }

    // height的getter和setter
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    // type的getter和setter
    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    // status的getter和setter
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString方法，用于打印楼宇信息
    @Override
    public String toString() {
        return "Building{" +
                "districtId=" + districtId +
                ", buildingId=" + buildingId +
                ", totalStorey=" + totalStorey +
                ", totalArea=" + totalArea +
                ", height=" + height +
                ", type=" + type +
                ", status='" + status + '\'' +
                '}';
    }
}
