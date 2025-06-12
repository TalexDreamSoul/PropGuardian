package dao.entity;

import dao.BaseEntity;
import lombok.Data;

import java.util.logging.Logger;

@Data
public class ConsumptionIndex extends BaseEntity {
    private long district_id; // 小区ID
    private long building_id; // 楼宇ID
    private long room_id;     // 业主ID
    private int date;        // 日期
    private double water_reading; // 用水量
    private double elec_reading;  // 用电量
    private double gas_reading;   // 用气量

    private final Logger logger = Logger.getLogger("ConsumptionIndex");

    public ConsumptionIndex() {
        super("consumptionindex");
    }

    public ConsumptionIndex(long district_id, long building_id, long room_id, int date, double water_reading, double elec_reading, double gas_reading) {
        super("consumptionindex");
        this.district_id = district_id;
        this.building_id = building_id;
        this.room_id = room_id;
        this.date = date;
        this.water_reading = water_reading;
        this.elec_reading = elec_reading;
        this.gas_reading = gas_reading;
    }

    @Override
    public boolean storage() {
        int i = insertOrUpdate(
                getEntity()
                        .set("district_id", this.district_id)
                        .set("building_id", this.building_id)
                        .set("room_id", this.room_id)
                        .set("date", this.date)
                        .set("water_reading", this.water_reading)
                        .set("elec_reading", this.elec_reading)
                        .set("gas_reading", this.gas_reading)
        );

        if ( i > 1 ) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }

        return i == 1;
    }
}
