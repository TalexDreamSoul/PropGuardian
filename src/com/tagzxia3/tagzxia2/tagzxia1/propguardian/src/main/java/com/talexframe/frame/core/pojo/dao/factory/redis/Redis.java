package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.redis;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.redis.RedisConfig;
import com.talexframe.frame.core.pojo.dao.DataProcessorStatus;
import com.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import com.talexframe.frame.core.pojo.dao.interfaces.IKVProcessor;
import com.talexframe.frame.core.pojo.wrapper.WrappedData;
import lombok.Getter;

import java.util.Objects;

/**
 * <br /> {@link com.talexframe.frame.core.pojo.dao.factory.redis Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 10:28 <br /> Project: TalexFrame <br />
 */
public class Redis implements IDataProcessor, IKVProcessor {

    @Getter
    private final RedisConfig config = RedisConfig.getInstance();
    private DataProcessorStatus status;

    public RedisConfig getConfig() {

        return config;

    }

    @Override
    public boolean connect() {

        // 自动连接
        status = DataProcessorStatus.CONNECTED;

        return true;

    }

    @Override
    public boolean disconnect() {

        config.getFactory().getConnection().close();

        status = DataProcessorStatus.DISCONNECTED;

        return true;

    }

    @Override
    public DataProcessorStatus getStatus() {

        return status;
    }

    @Override
    public boolean checkStatus() {

        status = Objects.requireNonNull(config.getFactory().getConnection().ping()).equalsIgnoreCase("pong") ? DataProcessorStatus.CONNECTED : DataProcessorStatus.DISCONNECTED;

        return status == DataProcessorStatus.CONNECTED;
    }

    @Override
    public void reConnect() {

        // 自动重连

    }

    @Override
    public boolean doCrash() {

        return true;
    }

    @Override
    public boolean joinPoint(String key, String value) {

        return false;
    }

    @Override
    public Object getPoint(String key) {

        return null;
    }

    @Override
    public WrappedData<?> getPointTyped(String key, Class<?> clz) {

        return null;
    }

    @Override
    public WrappedData<?> getPoint(String key, WrappedData<?> defaultValue) {

        return null;
    }

    @Override
    public boolean deletePoint(String key) {

        return false;
    }

    @Override
    public boolean updatePoint(String key, String value) {

        return false;
    }

}
