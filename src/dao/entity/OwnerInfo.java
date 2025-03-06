package dao.entity;

public class OwnerInfo {
    private int districtId;   // 小区ID
    private int buildingId;   // 楼宇ID
    private int roomId;       // 业主ID
    private double area;      // 面积
    private String status;    // 状态
    private String purpose;   // 用途
    private String oname;     // 业主名称
    private String sex;       // 性别
    private String idNum;     // 证件号
    private String address;   // 地址
    private String phone;     // 电话

    // 构造函数
    public OwnerInfo(int districtId, int buildingId, int roomId, double area, String status, String purpose, String oname, String sex, String idNum, String address, String phone) {
        this.districtId = districtId;
        this.buildingId = buildingId;
        this.roomId = roomId;
        this.area = area;
        this.status = status;
        this.purpose = purpose;
        this.oname = oname;
        this.sex = sex;
        this.idNum = idNum;
        this.address = address;
        this.phone = phone;
    }

    // Getter 和 Setter 方法
    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "OwnerInfo{" +
                "districtId=" + districtId +
                ", buildingId=" + buildingId +
                ", roomId=" + roomId +
                ", area=" + area +
                ", status='" + status + '\'' +
                ", purpose='" + purpose + '\'' +
                ", oname='" + oname + '\'' +
                ", sex='" + sex + '\'' +
                ", idNum='" + idNum + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
