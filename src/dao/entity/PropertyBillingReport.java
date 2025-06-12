package dao.entity;

import dao.BaseEntity;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.logging.Logger;

@Data
public class PropertyBillingReport extends BaseEntity {
    private int property_id; // 小区ID
    private int owner_id; // 楼宇ID
    private float billing_date; // 楼层数
    private double amount;

    private final Logger logger = Logger.getLogger("PropertyBillingReport");

    // 构造函数
    public PropertyBillingReport(int property_id, int  owner_id, float billing_date,double amount) {
        super("property_billing");

        this.property_id = property_id;
        this.owner_id = owner_id;
        this.billing_date =billing_date;
        this.amount = amount;
    }

    public PropertyBillingReport() {
        super("property_billing");
    }

    @SneakyThrows
    @Override
    public boolean storage() {
        int i = insertOrUpdate(
                getEntity()
                        .set("property_id", this.property_id)
                        .set("owner_id", this.owner_id)
                        .set("billing_date", this.billing_date)

        );

        if ( i > 1 ) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }

        return i == 1;
    }
}
