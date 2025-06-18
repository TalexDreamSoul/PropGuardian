package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.subapp;

import cn.hutool.core.date.TimeInterval;
import com.talexframe.frame.core.modules.application.TApp;
import com.talexframe.frame.core.modules.network.connection.IRequestReceiver;
import com.talexframe.frame.core.modules.network.connection.TRequest;
import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import com.talexframe.frame.core.pojo.wrapper.ResultData;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 12:08 Project: TalexFrame
 */
@Getter
@Slf4j
public class MethodAppReceiver extends FrameCreator implements IRequestReceiver {

    static final TimeInterval timer = new TimeInterval();

    private final TApp ownApp;
    private final Class<?> ownClass;
    private final Method method;
    private final TRequest tRequest;

    public MethodAppReceiver(TApp ownApp, Method method) {

        super("MethodAppReceiver", ownApp.getClass().getName());

        this.ownApp = ownApp;
        this.method = method;
        this.ownClass = ownApp.getClass();

        this.tRequest = method.getAnnotation(TRequest.class);

    }

    private boolean parseData = false;

    public void setParseData(boolean parseData, String reason) {

        log.debug("[解析层] setParseData: " + parseData + " | for: {}", reason);

        this.parseData = parseData;

    }

    public Object onRequest(ClassAppReceiver clzAppReceiver, WrappedResponse wr, long time) {

        log.info("[应用层] 交付执行 @" + clzAppReceiver.getOwnClass().getName() + "." + this.getMethod().getName());

        log.info("[应用层]  #  参数一览 #");
        log.info("[应用层]  #    (provide)-> {}", wr.getParams());
        log.info("[应用层]  #    (receive)-> {}", (Object) this.getMethod().getParameters());
        log.info("[应用层]  #  开始执行并计时 #");
        log.info("[应用层]  ------------------------");

        try {

            String requestID = wr.getRequest().getSession().getId();

            timer.start(requestID);

            Object obj = method.invoke(ownApp, wr.getParams().toArray());

            long ms = timer.intervalMs(requestID);

            if ( ms > 300 ) {

                log.warn("[应用层] 处理完毕 - 耗时较长，请优化接口！ 耗时: " + ms + "ms");

            } else {

                log.info("[应用层] 处理完毕 耗时: " + ms + "ms");

            }

            log.info("[解析层] Access in " + (System.currentTimeMillis() - time) + "ms totally");

            log.info("[应用层]  应用执行完毕!");
            log.info("[应用层] ------------------------");

            if ( !(obj instanceof ResultData ) ) {

                obj = ResultData.SUCCESS(obj);

            }

            return obj;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

}
