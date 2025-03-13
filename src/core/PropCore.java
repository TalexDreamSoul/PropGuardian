package core;

import configuration.Env;

import java.util.logging.Logger;

public class PropCore {

    private final Logger logger = Logger.getLogger("PropGuardian");

    private Env env;

    public PropCore() {

    }

    public void run() {
        logger.info("System running start");

        env = Env.getInstance();
        env.loadEnvProperties();

        this.setup();

        logger.info("System running end");
    }

    private void setup() {
        logger.info("Setup basic information...");

        String name = this.env.getProperty("name");

        logger.info("Load system name as " + name);
    }

}
