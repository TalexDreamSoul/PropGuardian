package dao.entity;

import cn.hutool.db.Entity;
import dao.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.logging.Logger;

@Data
public class UserInfo extends BaseEntity {

    private int id;
    private String uname;
    private String paswrd;
    private int purview;

    private final Logger logger = Logger.getLogger("UserInfo");

    // 构造函数
    public UserInfo(int id, String uname, String paswrd, int purview) {
        super("UserInfo");

        this.id = id;
        this.uname = uname;
        this.paswrd = paswrd;
        this.purview = purview;
    }

    public UserInfo() {
        super("UserInfo");
    }

    @SneakyThrows
    @Override
    public boolean storage() {
        Entity entity = getEntity()
                .set("uname", this.uname)
                .set("paswrd", this.paswrd)
                .set("purview", this.purview);

        if ( this.id != -1 ) {
            entity.set("id", this.id);
        }

        int i = insertOrUpdate(entity);

        if ( i > 1 ) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }

        return i == 1;
    }
}
