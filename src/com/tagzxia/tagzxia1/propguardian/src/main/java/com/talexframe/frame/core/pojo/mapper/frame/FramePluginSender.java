package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.mapper.frame;

import com.talexframe.frame.core.function.command.IConsoleSender;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.extern.slf4j.Slf4j;

/**
 * 框架发出者 <br /> {@link com.talexframe.frame.core.pojo.mapper.frame Package }
 *
 * @author TalexDreamSoul
 * 2022/07/25 18:42 <br /> Project: TalexFrame <br />
 */
@Slf4j
public final class FramePluginSender extends FrameCreator implements IConsoleSender {

    public FramePluginSender(WebPlugin webPlugin) {

        super("SENDER", webPlugin.getName());

    }

    @Override
    public void sendConsoleMessage(String... message) {

        for ( String msg : message ) {

            log.info("[" + getProvider() + "] " + msg);

        }

    }

    @Override
    public void warnConsoleMessage(String... message) {

        for ( String msg : message ) {

            log.warn("[" + getProvider() + "] " + msg);

        }
    }

    @Override
    public void errorConsoleMessage(String... message) {

        for ( String msg : message ) {

            log.error("[" + getProvider() + "] " + msg);

        }

    }

    @Override
    public void errorConsoleMessage(String message, Throwable throwable) {

        log.error("[" + getProvider() + "] " + message, throwable);

    }

    @Override
    public void debugConsoleMessage(String... message) {

        for ( String msg : message ) {

            log.debug("[" + getProvider() + "] " + msg);

        }

    }

    @Override
    public void debugConsoleMessage(String message, Object object) {

        log.debug("[" + getProvider() + "] " + message, object);

    }

    @Override
    public void sendMessage(String... message) {

        this.sendConsoleMessage("[" + getProvider() + "] " + message);

    }

}
