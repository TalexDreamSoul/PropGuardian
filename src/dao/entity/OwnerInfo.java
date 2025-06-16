package dao.entity;

import lombok.Data;
import dao.BaseEntity;
import lombok.SneakyThrows;
import lombok.Data;
import java.util.logging.Logger;

@Data
public class OwnerInfo extends BaseEntity {
    private short district_id;   // 小区ID
    private short building_id;   // 楼宇ID
    private short room_id;       // 业主ID
    private double area;      // 面积
    private String status;    // 状态
    private String purpose;   // 用途
    private String oname;     // 业主名称
    private String sex;       // 性别
    private String id_num;     // 证件号
    private String address;   // 地址
    private String phone;     // 电话

    // 构造函数
    public OwnerInfo(short district_id, short building_id, short room_id, double area, String status, String purpose, String oname, String sex, String id_num, String address, String phone) {
        super("owner_info");
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
    // Getter 和 Setter 方法
    private final Logger logger = Logger.getLogger("owner_info");

    public OwnerInfo() {super("owner_info");}


    @SneakyThrows
    @Override
    public boolean storage() {
        int i = insertOrUpdate(
                getEntity()
                        .set("district_id", this.district_id)
                        .set("building_id", this.building_id)
                        .set("status", this.status)
        );

        if ( i > 1 ) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }

        return i == 1;
    }
}
