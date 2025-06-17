package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao;

import lombok.Getter;

/**
 * <br /> {@link com.talexframe.frame.core.pojo.dao Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:28 <br /> Project: TalexFrame <br />
 */
public enum DataProcessorStatus {

    /**
     * 已连接
     */
    CONNECTED(1),

    /**
     * 连接失败
     */
    FAILED_CONNECT(-1),

    /**
     * 未连接
     */
    DISCONNECTED(0),

    ;

    @Getter
    private final int code;


    DataProcessorStatus(int code) {

        this.code = code;

    }

}
