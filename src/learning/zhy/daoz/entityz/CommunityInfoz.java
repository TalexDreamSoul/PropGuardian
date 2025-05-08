package learning.zhy.daoz.entityz;

public class CommunityInfoz {
    // 小区ID
    private long districtId;
    // 小区名称
    private String districtName;
    // 地址
    private String address;
    // 小区面积
    private double floorSpace;

    // 构造函数
    public CommunityInfoz(long districtId, String districtName, String address, double floorSpace) {
        this.districtId = districtId;
        this.districtName = districtName;
        this.address = address;
        this.floorSpace = floorSpace;
    }

    // Getter 和 Setter 方法
    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getFloorSpace() {
        return floorSpace;
    }

    public void setFloorSpace(double floorSpace) {
        this.floorSpace = floorSpace;
    }

    // toString 方法，用于打印对象信
    public String toString() {
        return "CommunityInfo{" +
                "districtId=" + districtId +
                ", districtName='" + districtName + '\'' +
                ", address='" + address + '\'' +
                ", floorSpace=" + floorSpace +
                '}';
    }
}

