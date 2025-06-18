package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.function.listener;

import cn.hutool.core.thread.ThreadUtil;
import com.talexframe.frame.TalexFrameApplication;
import com.talexframe.frame.core.modules.event.events.frame.*;
import com.talexframe.frame.core.pojo.enums.FrameStatus;
import com.talexframe.frame.core.talex.TFrame;
import com.talexframe.launcher.Launcher;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * 框架内部主要的监听器 <br /> {@link com.talexframe.frame.core.function.listener Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/19 12:07 <br /> Project: TalexFrame <br />
 */
@Component
@Slf4j
public class FrameMajorListener {

    /**
     * SpringContext BeforeRefresh (Bean Definitions Loaded)
     */
    @EventListener
    public void onStart(ApplicationPreparedEvent event) {

        TFrame tframe = TFrame.tframe;
        tframe.setFrameStatus(FrameStatus.PREPARING);

        tframe.callEvent(new FramePreInstallEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

    }

    /**
     * SpringContext refreshed after (CommandLine Runners before)
     */
    @EventListener
    public void onStart(ApplicationStartedEvent event) {

        TFrame.tframe.setFrameStatus(FrameStatus.STARTING);

        TFrame.tframe.callEvent(new FramePostInstallEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

    }

    /**
     * After any command-line runners
     */
    @EventListener
    public void onStart(ApplicationReadyEvent event) {

        TFrame.tframe.callEvent(new FrameStartedEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

        TFrame.tframe.started();

    }

    /**
     * SpringBoot Application Stopped
     */
    @EventListener
    public void onStopped(ContextStoppedEvent event) {

        TFrame.tframe.setFrameStatus(FrameStatus.STOPPING);

        TFrame.tframe.callEvent(new FramePreUnInstallEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

        // TFrame.tframe.shutdown();
    }

    /**
     * SpringBoot Application Closed
     */
    @SneakyThrows
    @EventListener
    public void onClosed(ContextClosedEvent event) {

        log.warn("框架已停止.");

        TFrame.tframe.callEvent(new FramePostUnInstallEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

        if ( TFrame.tframe.getFrameStatus() == FrameStatus.CRASHED ) {

            log.warn("键入任意字符关闭系统!");

            Scanner scanner = new Scanner(System.in);

            scanner.nextLine();

            scanner.close();

        } else if( Launcher.restart ) {

            ThreadUtil.execAsync(() -> {

                log.info("[框架] 将在 3 秒后重启...");

                try {
                    Thread.sleep(3000);
                } catch ( InterruptedException e ) {
                    throw new RuntimeException(e);
                }

                TalexFrameApplication.start(Launcher.g_args);

            });

        }

    }

}
