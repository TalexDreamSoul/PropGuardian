package dao.entity;

import dao.BaseEntity;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.logging.Logger;

@Data
public class Olinerepairservice extends BaseEntity {
    private String Community;
    private String Owner;
    private String Type;
    private String Description;
    private String Status;


    private final Logger logger = Logger.getLogger("Olinerepairservice");

    // 构造函数
    public Olinerepairservice(String Community , String Owner, String Type, String Description , String Status) {
        super("olinerepairservice");
        this.Community=Community;
        this.Owner=Owner;
        this.Type=Type;
        this.Description=Description;
        this.Status=Status;


    }

    public Olinerepairservice() {
        super("olinerepairservice");
    }

    @SneakyThrows
    @Override
    public boolean storage() {
        int i = insertOrUpdate(
                getEntity()
                        .set("Community", this.Community)
                        .set("Owner", this.Owner)
                        .set("Type", this.Type)
                        .set("Description", this.Description)
                        .set("Status", this.Status)
        );



        if ( i > 1 ) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }

        return i == 1;
    }
}
