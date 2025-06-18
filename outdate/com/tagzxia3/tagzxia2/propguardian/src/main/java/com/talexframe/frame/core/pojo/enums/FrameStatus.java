package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.enums;

import lombok.Getter;

/**
 * 框架状态 <br /> {@link com.talexframe.frame.pojo.enums Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/19 20:44 <br /> Project: TalexFrame <br />
 */
@Getter
public enum FrameStatus {

    /**
     * 已停止
     */
    STOPPED("已停止"),

    /**
     * 停止中
     */
    STOPPING("停止中"),

    /**
     * 准备中
     */
    PREPARING("准备中"),

    /**
     * 运行中
     */
    RUNNING("运行中"),

    /**
     * 启动中
     */
    STARTING("启动中"),
    /**
     * 状态异常
     */
    ERROR("状态异常"),

    /**
     * 崩溃
     **/
    CRASHED("崩溃");

    /**
     * 获取当前状态消息
     */
    private final String msg;

    FrameStatus(String msg) {

        this.msg = msg;

    }

}
