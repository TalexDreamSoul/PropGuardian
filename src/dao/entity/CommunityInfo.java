package dao.entity;

import dao.BaseEntity;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.logging.Logger;

@Data
public class CommunityInfo extends BaseEntity {
    // 小区ID
    private long district_id;
    // 小区名称
    private String district_name;
    // 地址

    private String address;
    // 小区面积
    private double floor_space;

    private final Logger logger = Logger.getLogger("CommunityInfo");

    // 构造函数
    public CommunityInfo(long district_id, String district_name, String address, double floor_space) {
        super("community_info");

        this.district_id = district_id;
        this.district_name = district_name;
        this.address = address;
        this.floor_space = floor_space;
    }

    public CommunityInfo() {
        super("community_info");
    }

    @Override
    public boolean storage() {
        int i = insertOrUpdate(
                getEntity()
                        .set("district_id", this.district_id)
                        .set("district_name", this.district_name)
                        .set("address", this.address)
                        .set("floor_space", this.floor_space)
        );

        if (i > 0) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }

        return i == 1;
    }
}

