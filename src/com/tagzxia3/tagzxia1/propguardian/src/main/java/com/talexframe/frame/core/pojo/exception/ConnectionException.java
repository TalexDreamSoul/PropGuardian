package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.exception;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.exception.FrameException;
import com.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import lombok.Getter;

/**
 * {@link com.talexframe.frame.exception Package }
 *
 * @author TalexDreamSoul 22/03/12 下午 08:30 Project: TalexFrame
 */
public class ConnectionException extends FrameException {

    @Getter
    private final IDataProcessor processor;

    public ConnectionException(IDataProcessor processor, String message) {

        super("在连接时发生异常: " + message);

        this.processor = processor;

    }

}
