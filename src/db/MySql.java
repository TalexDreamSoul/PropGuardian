package db;

import configuration.Env;

public class MySql {
    public MySql() {
        Env instance = Env.getInstance();
        String url = instance.getProperty("mysql");
    }

    public void connect() {

    }

    public void disconnect() {

    }
}