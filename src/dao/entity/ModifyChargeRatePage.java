package dao.entity;

import dao.BaseEntity;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.logging.Logger;

@Data
public class ModifyChargeRatePage extends BaseEntity {
    private String item; // 小区ID
    private String unit; // 楼宇ID
    private float price; // 楼层数


    private final Logger logger = Logger.getLogger("ModifyChargeRatePage");

    // 构造函数
    public ModifyChargeRatePage(String item, String unit, float price) {
        super("modifycharge_ratepage");

        this.item = item;
        this.unit = unit;
        this.price = price;

    }

    public ModifyChargeRatePage() {
        super("modifycharge_ratepage");
    }

    @SneakyThrows
    @Override
    public boolean storage() {
        int i = insertOrUpdate(
                getEntity()
                        .set("item", this.item)
                        .set("unti", this.unit)
                        .set("price", this.price)

        );

        if ( i > 1 ) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }

        return i == 1;
    }
}
