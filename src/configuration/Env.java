package configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Env {

    private static final Logger LOGGER = Logger.getLogger(Env.class.getName());
    private static volatile Env instance;
    private Map<String, String> envMap;

    private Env() {
    }

    public static Env getInstance() {
        if (instance == null) {
            synchronized (Env.class) {
                if (instance == null) {
                    instance = new Env();
                }
            }
        }
        return instance;
    }

    public void loadEnvProperties() {
        Properties properties = new Properties();

        try {
            String path = getClass().getResource("").getPath();
            File target = new File(path, "resources/.env");
            InputStream inputStream = new FileInputStream(target);

            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading env properties", e);
        }
        envMap = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            envMap.put(key, properties.getProperty(key));
        }

        LOGGER.info(("Load " + envMap.size() + " properties done."));
    }

    public String getProperty(String key) {
        return envMap.get(key);
    }

}