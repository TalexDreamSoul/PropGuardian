package dao.entity;

import cn.hutool.db.Entity;
import dao.BaseEntity;
import dao.upper.IDataStorage;
import db.MySql;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.logging.Logger;

@Getter
public class UserInfo extends BaseEntity {

    private String uname;
    private String paswrd;
    private int purview;

    private final Logger logger = Logger.getLogger("UserInfo");

    // 构造函数
    public UserInfo(String uname, String paswrd, int purview) {
        super("userinfo");

        this.uname = uname;
        this.paswrd = paswrd;
        this.purview = purview;
    }

    public UserInfo() {
        super("userinfo");
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uname='" + uname + '\'' +
                ", paswrd='" + paswrd + '\'' +
                ", purview=" + purview +
                '}';
    }

    @SneakyThrows
    @Override
    public boolean storage() {
        int i = insertOrUpdate(getEntity().set("uname", this.uname)
                .set("paswrd", this.paswrd)
                .set("purview", this.purview));

        if ( i > 1 ) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls!");
        }

        return i == 1;
    }
}
