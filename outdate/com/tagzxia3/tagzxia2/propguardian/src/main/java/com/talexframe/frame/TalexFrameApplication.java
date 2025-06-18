package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author TalexDreamSoul
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
public class TalexFrameApplication {

    public static final long startedTimeStamp = System.nanoTime();
    public static ConfigurableApplicationContext context;

    public static void start(String[] args) {

        context = SpringApplication.run(TalexFrameApplication.class, args);

        context.setId("TalexFrameApp");

    }

}
