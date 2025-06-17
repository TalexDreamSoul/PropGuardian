package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.talex;

import cn.hutool.core.thread.ThreadUtil;
import com.google.common.collect.Multimap;
import com.talexframe.frame.TalexFrameApplication;
import com.talexframe.frame.core.function.command.CommandManager;
import com.talexframe.frame.core.function.command.TCmdCompAdapter;
import com.talexframe.frame.core.function.command.frame.*;
import com.talexframe.frame.core.function.listener.FrameSelfListener;
import com.talexframe.frame.core.modules.application.TAppCompAdapter;
import com.talexframe.frame.core.modules.application.TAppManager;
import com.talexframe.frame.core.modules.event.FrameListener;
import com.talexframe.frame.core.modules.event.MethodManager;
import com.talexframe.frame.core.modules.event.TalexEventBus;
import com.talexframe.frame.core.modules.event.events.frame.FrameStartedEvent;
import com.talexframe.frame.core.modules.event.service.TalexEvent;
import com.talexframe.frame.core.modules.network.connection.NetworkListener;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddonAdapter;
import com.talexframe.frame.core.modules.plugins.adapt.config.json.JSONConfigAdapter;
import com.talexframe.frame.core.modules.plugins.adapt.config.yaml.YamlConfigAdapter;
import com.talexframe.frame.core.modules.plugins.addon.FramePluginListener;
import com.talexframe.frame.core.modules.plugins.addon.TPluginListenerCompAdapter;
import com.talexframe.frame.core.modules.plugins.core.PluginInfo;
import com.talexframe.frame.core.modules.plugins.core.PluginManager;
import com.talexframe.frame.core.modules.repository.TRepoCompAdapter;
import com.talexframe.frame.core.modules.repository.TRepoManager;
import com.talexframe.frame.core.pojo.dao.factory.DAOManager;
import com.talexframe.frame.core.pojo.enums.FrameStatus;
import com.talexframe.frame.core.pojo.mapper.frame.FrameSender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * <br /> {@link com.talexframe.frame.core.talex Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/18 22:10 <br /> Project: TalexFrame <br />
 */
@Getter
@Slf4j
@SuppressWarnings( "unused" )
public class TFrame {

    public static final TFrame tframe = new TFrame();
    @Getter
    private static final File mainFile = new File(System.getProperty("user.dir"));
    private final PluginInfo.PluginSupportVersion versionE = PluginInfo.PluginSupportVersion.SEVEN_OFFICIAL;
    private final String version = "7.0.0";
    private final FrameSender frameSender;
    @Getter( AccessLevel.PACKAGE )
    private final TalexEventBus eventBus = TalexEventBus.getDefault();
    @Setter
    private FrameStatus frameStatus = FrameStatus.STOPPED;
    private boolean started = false;
    private CommandManager commandManager;
    private DAOManager daoManager;
    private PluginManager pluginManager;
    private TRepoManager repoManager;
    private TAppManager appManager;

    private TFrame() {

        this.printBanner();

        this.frameSender = FrameSender.getDefault();

    }

    private void printBanner() {

        System.out.println("                                                                                 \n" +
                " /__  ___/                            //   / /                                   \n" +
                "   / /   ___     //  ___             //___   __      ___      _   __      ___    \n" +
                "  / /  //   ) ) // //___) ) \\\\ / /  / ___  //  ) ) //   ) ) // ) )  ) ) //___) ) \n" +
                " / /  //   / / // //         \\/ /  //     //      //   / / // / /  / / //        \n" +
                "/ /  ((___( ( // ((____      / /\\ //     //      ((___( ( // / /  / / ((____     \n" +
                "                                                                                 \n" +
                "    \n" +
                " _____ ___   ___  \n" +
                "|___  / _ \\ / _ \\ \n" +
                "   / / | | | | | |\n" +
                "  / /| |_| | |_| |\n" +
                " /_(_)\\___(_)___/ \n" +
                "                  \n                                                 ");

    }

    /**
     * 启动整体架构
     */
    public void started() {

        if ( started ) {
            return;
        }

        started = true;
        setFrameStatus(FrameStatus.RUNNING);

        log.info("Loading repo & application ...");
        this.repoManager = TRepoManager.init();
        this.appManager = TAppManager.init();

        log.info("Loading dao-manager ...");
        this.daoManager = new DAOManager();


        getEventBus().registerListener(new FrameSelfListener());

        callEvent(new FrameStartedEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

        if ( frameStatus == FrameStatus.RUNNING ) {

            log.info("Loading command-manager ...");

            CommandManager.initial();
            this.commandManager = CommandManager.INSTANCE;
            this.commandManager.setCommandExecutor("help", new HelpCmd());
            this.commandManager.setCommandExecutor("plugin", new PluginCmd());
            this.commandManager.setCommandExecutor("stop", new StopCmd());
            this.commandManager.setCommandExecutor("restart", new RestartCmd());
            this.commandManager.setCommandExecutor("info", new InfoCmd());

            new TCmdCompAdapter();

        }

        if ( frameStatus == FrameStatus.RUNNING ) {

            getEventBus().registerListener(new NetworkListener());

            new TAppCompAdapter();
            new TRepoCompAdapter();
            new TPluginListenerCompAdapter();
            new ReceiverAddonAdapter();

            log.info("Loading plugin-manager ...");

            new YamlConfigAdapter();
            new JSONConfigAdapter();
            // ThreadUtil.execAsync(() -> this.pluginManager.loadAllPluginsInFolder());

            ThreadUtil.execute(() -> {

                this.pluginManager = new PluginManager(new File(mainFile.getAbsolutePath() + "/plugins"));

                this.pluginManager.loadAllPluginsInFolder();

            });

            log.info("框架启动成功! (" + (System.nanoTime() - TalexFrameApplication.startedTimeStamp) + " ns)");
            log.debug("** DEBUG 模式已启动!");

        }

    }

    public void crash(Throwable e) {

        shutdown();

        frameStatus = FrameStatus.CRASHED;

        log.error("框架运行失败, 详情: ", e);

    }

    public synchronized void shutdown() {

        if ( frameStatus == FrameStatus.STOPPED ) {

            return;

        }

        TalexFrameApplication.context.stop();

        log.warn("正在关闭服务器... (" + frameStatus.name() + ")");

        if ( pluginManager != null ) {

            log.info("[插件] 正在卸载插件...");

            for ( String plugin : pluginManager.getPluginHashMap().keySet() ) {

                log.info("[插件] 卸载插件: " + plugin);

                pluginManager.unloadPlugin(plugin);

                log.info("[插件] 插件卸载完成: " + plugin);

            }

            log.info("[插件] 所有插件卸载完成!");

        }

        if ( daoManager != null ) {

            log.info("[数据库] 正在关闭数据库连接...");

            daoManager.shutdown();

            log.info("[数据库] 所有数据库连接已关闭!");

        }

        log.warn("服务器已关闭!");

    }

    public TFrame callEvent(TalexEvent event) {

        assert eventBus != null;
        eventBus.callEvent(event);

        return this;

    }

    public TFrame registerListener(FramePluginListener listener) {

        assert eventBus != null;
        eventBus.registerListener(listener);

        return this;

    }

    public TFrame unRegisterListener(FramePluginListener listener) {

        assert eventBus != null;
        eventBus.unRegisterListener(listener);

        return this;

    }

    public Multimap<FrameListener, MethodManager> getListeners() {

        assert eventBus != null;
        return eventBus.getListenerManager();

    }

}
