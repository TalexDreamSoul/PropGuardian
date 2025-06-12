package dao.entity;

import dao.BaseEntity;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.logging.Logger;

@Data
public class Olinerepairservice extends BaseEntity {
    private String Community; // 小区ID
    private String Owner; // 楼宇ID
    private String Type; // 楼层数
    private String Description; // 总面积
    private String Status; // 楼高


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
