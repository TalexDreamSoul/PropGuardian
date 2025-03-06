package dao.entity;

public class CommunityUse {
    // 小区ID
    private long districtId;
    // 日期
    private int dateInt11;
    // 用水量
    private double totWaterReading;
    // 用电量
    private double totElecReading;
    // 用气量
    private double secSupplyReading;

    // 构造函数
    public CommunityUse(long districtId, int dateInt11, double totWaterReading, double totElecReading, double secSupplyReading) {
        this.districtId = districtId;
        this.dateInt11 = dateInt11;
        this.totWaterReading = totWaterReading;
        this.totElecReading = totElecReading;
        this.secSupplyReading = secSupplyReading;
    }

    // Getter 和 Setter 方法
    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public int getDateInt11() {
        return dateInt11;
    }

    public void setDateInt11(int dateInt11) {
        this.dateInt11 = dateInt11;
    }

    public double getTotWaterReading() {
        return totWaterReading;
    }

    public void setTotWaterReading(double totWaterReading) {
        this.totWaterReading = totWaterReading;
    }

    public double getTotElecReading() {
        return totElecReading;
    }

    public void setTotElecReading(double totElecReading) {
        this.totElecReading = totElecReading;
    }

    public double getSecSupplyReading() {
        return secSupplyReading;
    }

    public void setSecSupplyReading(double secSupplyReading) {
        this.secSupplyReading = secSupplyReading;
    }

    // toString 方法，用于打印对象信息
    public String toString() {
        return "CommunityUse{" +
                "districtId=" + districtId +
                ", dateInt11=" + dateInt11 +
                ", totWaterReading=" + totWaterReading +
                ", totElecReading=" + totElecReading +
                ", secSupplyReading=" + secSupplyReading +
                '}';
    }
}
