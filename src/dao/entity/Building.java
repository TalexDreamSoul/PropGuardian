package dao.entity;

import dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.logging.Logger;

@Data
public class Building extends BaseEntity {
    private short district_id; // 小区ID
    private short building_id; // 楼宇ID
    private short total_storey; // 楼层数
    private double total_area; // 总面积
    private double height; // 楼高
    private short type; // 类型
    private String status; // 状态

    private final Logger logger = Logger.getLogger("Building");

    // 构造函数
    public Building(short district_id, short building_id, short total_storey, double total_area, double height, short type, String status) {
        super("building_info");

        this.district_id = district_id;
        this.building_id = building_id;
        this.total_storey = total_storey;
        this.total_area = total_area;
        this.height = height;
        this.type = type;
        this.status = status;
    }

    public Building() {
        super("building_info");
    }

    @SneakyThrows
    @Override
    public boolean storage() {
        int i = insertOrUpdate(
                getEntity()
                        .set("district_id", this.district_id)
                        .set("building_id", this.building_id)
                        .set("total_storey", this.total_storey)
                        .set("total_area", this.total_area)
                        .set("height", this.height)
                        .set("type", this.type)
                        .set("status", this.status)
        );

        if ( i > 1 ) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }

        return i == 1;
    }
}
