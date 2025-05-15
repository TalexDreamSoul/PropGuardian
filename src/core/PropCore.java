package core;

import configuration.Env;
import dao.DaoInit;
import db.MySql;
import gui.LoginPanel;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

@Getter
public class PropCore {

    public static PropCore INS = new PropCore();
    private final Logger logger = Logger.getLogger("PropGuardian");

    private Env env;
    private MySql mySql;
    private DaoInit daoInit = new DaoInit();

    public PropCore() {

    }

    public void run() {
        logger.info("System running start");

        env = Env.getInstance();
        env.loadEnvProperties();

        this.setup();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new LoginPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            registerWindowCloseListener(frame);
        });

        logger.info("System running end");
    }

    private void setup() {
        logger.info("Setup basic information...");

        String name = this.env.getProperty("name");

        logger.info("Load system name as " + name);

        this.mysqlSetup();

        this.daoInit.init(this);
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

    public void destroy() {
        this.mySql.disconnect();

        logger.info("System destroy");
    }

    private void registerWindowCloseListener(JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                destroy();
            }
        });
    }

    public MySql getMySql() {
        return mySql;
    }
}
