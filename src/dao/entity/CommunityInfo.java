package dao.entity;

import lombok.Data;
import lombok.SneakyThrows;
@Data
public class CommunityInfo {
    // 小区ID
    private long district_id;
    // 小区名称
    private String district_name;
    // 地址

    private String address;
    // 小区面积
    private double floor_space;

    // 构造函数
    public CommunityInfo(long districtId, String district_name, String address, double floor_space) {
        this.district_id = district_id;
        this.district_name = district_name;
        this.address = address;
        this.floor_space = floor_space;
    }
}

