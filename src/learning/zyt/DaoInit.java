package learning.zyt;

import cn.hutool.db.ds.simple.SimpleDataSource;
import core.PropCore;
import dao.entity.UserInfo;
import db.MySql;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class DaoInit {

    private final Logger logger = Logger.getLogger("DaoInit");

    public void init(PropCore core) {
        // 假设数据库已经有这些表

        // User Init
        this.userInit(core);
    }

    @SneakyThrows
    public void userInit(PropCore core) {
        MySql mySql = core.getMySql();
        SimpleDataSource simpleDataSource = mySql.getSimpleDataSource();

        String sql = "SELECT * FROM `userinfo`";

        PreparedStatement preparedStatement = simpleDataSource.getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        if ( resultSet.next() ) {
            return;
        } else {
            // Create New User
            UserInfo info = new UserInfo("admin", "123456", 1);

            info.storage(mySql);

            logger.info("[DaoInit] Init base user info done!");
        }
    }

}
