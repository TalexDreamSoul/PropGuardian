package dao.entity;

import dao.BaseEntity;
import lombok.Data;

import java.util.logging.Logger;

@Data
public class PriceType extends BaseEntity {
    private short charge_id;
    private String charge_name;
    private double unit_price;

    private final Logger logger = Logger.getLogger("PriceType");

    // 构造函数
    public PriceType(short charge_id, String charge_name, double unit_price) {
        super("PriceType");

        this.charge_id = charge_id;
        this.charge_name = charge_name;
        this.unit_price = unit_price;
    }

    public PriceType() {super("PriceType");}

    @Override
    public boolean storage() {
        int i = insertOrUpdate(
                getEntity()
                        .set("charge_id", this.charge_id)
                        .set("charge_name", this.charge_name)
                        .set("unit_price", this.unit_price)
        );
        if ( i > 1 ) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }
        return i == 1;
    }
}
