package learning.lxl;

import cn.hutool.db.Db;
import cn.hutool.db.ds.simple.SimpleDataSource;
import configuration.Env;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySql {
    private final Logger logger = Logger.getLogger("MySQL");
    @Getter
    private SimpleDataSource simpleDataSource;

    public MySql() {
        Env instance = Env.getInstance();

        this.simpleDataSource = new SimpleDataSource(instance.getProperty("mysql"), instance.getProperty("user"), instance.getProperty("password"));
    }

    public Db use() {
        return
                Db.use(simpleDataSource);
    }

    public boolean connect() {
        try {
            Connection connection = this.simpleDataSource.getConnection();

            String _s = connection.nativeSQL("select *");

            return true;
        } catch (SQLException e) {
            logger.severe("Error connecting to MySQL: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        this.simpleDataSource.close();
    }
}
