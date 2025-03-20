package db;

import cn.hutool.db.ds.simple.SimpleDataSource;
import configuration.Env;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySql {
    private final Logger logger = Logger.getLogger("MySQL");
    private SimpleDataSource simpleDataSource;

    public MySql() {
        Env instance = Env.getInstance();

        this.simpleDataSource = new SimpleDataSource(instance.getProperty("mysql"), instance.getProperty("user"), instance.getProperty("password"));
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
