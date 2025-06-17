package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.function.command;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.function.command.ISender;

/**
 * ConsoleSender -> Sender接口 <br /> {@link com.talexframe.frame.function.command Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 10:41 <br /> Project: TalexFrame <br />
 */
public interface IConsoleSender extends ISender {

    /**
     * 向后台发出消息 (打印日志） INFO 级别
     *
     * @param message 要发出的消息
     */
    void sendConsoleMessage(String... message);

    /**
     * 向后台发出消息 (打印日志） WARN 级别
     *
     * @param message 要发出的消息
     */
    void warnConsoleMessage(String... message);

    /**
     * 向后台发出消息 (打印日志） ERROR 级别
     *
     * @param message 要发出的消息
     */
    void errorConsoleMessage(String... message);

    /**
     * 向后台发出消息 (打印日志） ERROR 级别
     *
     * @param message   要发出的消息
     * @param throwable 异常
     */
    void errorConsoleMessage(String message, Throwable throwable);

    /**
     * 向后台发出消息 (打印日志） DEBUG 级别
     *
     * @param message 要发出的消息
     */
    void debugConsoleMessage(String... message);

    /**
     * 向后台发出消息 (打印日志） DEBUG 级别
     *
     * @param message 要发出的消息
     * @param object  debug对象
     */
    void debugConsoleMessage(String message, Object object);

}
