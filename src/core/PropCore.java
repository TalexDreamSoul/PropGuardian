package core;

import configuration.Env;
import db.MySql;
import lombok.Getter;

import java.util.logging.Logger;

@Getter
public class PropCore {

    private final Logger logger = Logger.getLogger("PropGuardian");

    private Env env;
    private MySql mySql;

    public PropCore() {

    }

    public void run() {
        logger.info("System running start");

        env = Env.getInstance();
        env.loadEnvProperties();

        this.setup();
        this.mysqlSetup();

        logger.info("System running end");
    }

    private void setup() {
        logger.info("Setup basic information...");

        String name = this.env.getProperty("name");

        logger.info("Load system name as " + name);

    }

    private void mysqlSetup() {
        this.mySql = new MySql();

        if ( this.mySql.connect() == true ) {
            logger.info("Connected to MySQL");
        } else {
            logger.severe("Failed to connect to MySQL");
            System.exit(1);
        }
    }

}
