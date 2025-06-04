package dao.entity;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.SqlConnRunner;
import dao.upper.IDataStorage;
import db.MySql;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;

@Getter
public class UserInfo implements IDataStorage {

    private String uname;
    private String paswrd;
    private int purview;

    private final Logger logger = Logger.getLogger("UserInfo");

    // 构造函数
    public UserInfo(String uname, String paswrd, int purview) {
        this.uname = uname;
        this.paswrd = paswrd;
        this.purview = purview;
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
    public boolean storage(MySql mysql) {
        int i = mysql.use().insertOrUpdate(
                Entity.create("userinfo")
                        .set("uname", this.uname)
                        .set("paswrd", this.paswrd)
                        .set("purview", this.purview)
        );

        if ( i > 2 ) {
            logger.warning("[Storage] Take effects to multiple rows, attention pls! " + this);
        }

        return i == 1;
    }
}
