package dao.entity;

import lombok.Data;
import lombok.SneakyThrows;

@Data
public class OwnerInfo {
    private int district_id;   // 小区ID
    private int building_id;   // 楼宇ID
    private int room_id;       // 业主ID
    private double area;      // 面积
    private String status;    // 状态
    private String purpose;   // 用途
    private String oname;     // 业主名称
    private String sex;       // 性别
    private String id_num;     // 证件号
    private String address;   // 地址
    private String phone;     // 电话

    // 构造函数
    public OwnerInfo(int district_id, int building_id, int room_id, double area, String status, String purpose, String oname, String sex, String id_num, String address, String phone) {
        this.district_id = district_id;
        this.building_id = building_id;
        this.room_id = room_id;
        this.area = area;
        this.status = status;
        this.purpose = purpose;
        this.oname = oname;
        this.sex = sex;
        this.id_num = id_num;
        this.address = address;
        this.phone = phone;
    }
    @SneakyThrows
    @Override
    // Getter 和 Setter 方法


    public String toString() {
        return "OwnerInfo{" +
                "districtId=" + district_id +
                ", buildingId=" + building_id +
                ", roomId=" + room_id +
                ", area=" + area +
                ", status='" + status + '\'' +
                ", purpose='" + purpose + '\'' +
                ", oname='" + oname + '\'' +
                ", sex='" + sex + '\'' +
                ", idNum='" + id_num + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
