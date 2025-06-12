package dao.entity;

import dao.BaseEntity;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.logging.Logger;

@Data
public class Notificationpanel extends BaseEntity {
    private String Community;
    private String title;
    private String message;


    private final Logger logger = Logger.getLogger("Notificationpanel");

    // 构造函数
    public Notificationpanel(String Community , String title, String message) {
        super("notificationpanel");
        this.Community=Community;
        this.title=title;
        this.message=message;

    }

    public Notificationpanel() {
        super("notificationpanel");
    }

    @SneakyThrows
    @Override
    public boolean storage() {
        int i = insertOrUpdate(
                getEntity()
                        .set("Community", this.Community)
                        .set("title", this.title)
                        .set("message", this.message)

        );



        if ( i > 1 ) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }

        return i == 1;
    }
}
