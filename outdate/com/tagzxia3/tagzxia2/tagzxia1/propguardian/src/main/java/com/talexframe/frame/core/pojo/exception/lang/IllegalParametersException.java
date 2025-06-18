package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.exception.lang;

import com.talexframe.frame.core.pojo.exception.FrameException;

/**
 * <br /> {@link com.talexframe.frame.exception.lang Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 11:26 <br /> Project: TalexFrame <br />
 */
public class IllegalParametersException extends FrameException {

    public IllegalParametersException() {

        super("参数不合法");

    }

    /**
     * 通过给定数量创建一个细节异常
     *
     * @param amo     需求数量
     * @param provide 提供数量
     */
    public IllegalParametersException(int amo, int provide) {

        super("参数不合法，需要 " + amo + " 个参数，但只提供了 " + provide + " 个");

    }

}
