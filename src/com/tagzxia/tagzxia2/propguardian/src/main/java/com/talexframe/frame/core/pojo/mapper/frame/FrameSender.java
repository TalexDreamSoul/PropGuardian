package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.mapper.frame;

import com.talexframe.frame.core.function.command.IConsoleSender;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.extern.slf4j.Slf4j;

/**
 * 框架发出者 <br /> {@link com.talexframe.frame.mapper.frame Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 10:39 <br /> Project: TalexFrame <br />
 */
@Slf4j
public final class FrameSender extends FrameCreator implements IConsoleSender {

    private static FrameSender INSTANCE;

    private FrameSender() {

        super("SENDER", "FRAME");
    }

    public static FrameSender getDefault() {

        if ( INSTANCE == null ) {

            INSTANCE = new FrameSender();

        }

        return INSTANCE;

    }

    @Override
    public void sendConsoleMessage(String... message) {

        for ( String msg : message ) {

            log.info(msg);

        }

    }

    @Override
    public void warnConsoleMessage(String... message) {

        for ( String msg : message ) {

            log.warn(msg);

        }
    }

    @Override
    public void errorConsoleMessage(String... message) {

        for ( String msg : message ) {

            log.error(msg);

        }

    }

    @Override
    public void errorConsoleMessage(String message, Throwable throwable) {

        log.error(message, throwable);

    }

    @Override
    public void debugConsoleMessage(String... message) {

        for ( String msg : message ) {

            log.debug(msg);

        }

    }

    @Override
    public void debugConsoleMessage(String message, Object object) {

        log.debug(message, object);

    }

    @Override
    public void sendMessage(String... message) {

        this.sendConsoleMessage(message);

    }

}
