package dao.entity;

import dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.logging.Logger;

@Data
public class MeterReading extends BaseEntity {

    private String input_date; // 读数日期
    private String room_number; // 房间号
    private double water_reading; // 水表读数
    private double electric_reading; // 电表读数
    private double gas_reading; // 燃气表读数
    private int building_id; // 楼宇ID
    private int district_id; // 区域ID

    private final Logger logger = Logger.getLogger("MeterReading");

    // 构造函数
    public MeterReading(String input_date, String room_number, double water_reading, double electric_reading, double gas_reading, Integer building_id, Integer district_id) {
        super("meter_reading");

        this.input_date = input_date;
        this.room_number = room_number;
        this.water_reading = water_reading;
        this.electric_reading = electric_reading;
        this.gas_reading = gas_reading;
        this.building_id = building_id;
        this.district_id = district_id;
    }

    public MeterReading() {
        super("meter_reading");
    }

    @SneakyThrows
    @Override
    public boolean storage() {
        int i = insertOrUpdate(
                getEntity()
                        .set("input_date", this.input_date)
                        .set("room_number", this.room_number)
                        .set("water_reading", this.water_reading)
                        .set("electric_reading", this.electric_reading)
                        .set("gas_reading", this.gas_reading)
                        .set("building_id", this.building_id)
                        .set("district_id", this.district_id)
        );

        if (i > 1) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }

        return i == 1;
    }
}