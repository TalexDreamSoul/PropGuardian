package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory;

import cn.hutool.core.util.ClassUtil;
import com.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import com.talexframe.frame.core.talex.TFrame;
import com.talexframe.frame.core.pojo.exception.ConnectionException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * <br /> {@link com.talexframe.frame.core.pojo.dao.factory Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:39 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class DAOManager {

    private static final Map<Class<?>, IDataProcessor> daoMap = new HashMap<>();

    @SneakyThrows
    public DAOManager() {

        // start

        for ( Class<?> clz : ClassUtil.scanPackage(getClass().getPackage().getName(), DAOManager::check) ) {

            IDataProcessor processor = (IDataProcessor) clz.newInstance();

            if ( processor.connect() ) {

                daoMap.put(clz, processor);

                log.info("[DAO] " + clz.getSimpleName() + " connected.");

            } else {

                log.warn("[DAO] " + clz.getSimpleName() + " connect failed!");

                if ( processor.doCrash() ) {

                    TFrame.tframe.crash(new ConnectionException(processor, clz.getName() + " connect failed!"));

                }

            }

        }

    }

    private static boolean check(Class<?> clz) {

        return IDataProcessor.class.isAssignableFrom(clz);

    }

    public void shutdown() {

        daoMap.values().forEach(IDataProcessor::disconnect);

    }

    public static class ProcessorGetter<T> {

        Class<?> clz;

        public ProcessorGetter(Class<?> clz) {

            if ( !check(clz) ) {

                throw new RuntimeException("Class is not assignable from IDataProcessor");

            }

            this.clz = clz;

        }

        public T getProcessor() {

            return (T) daoMap.get(clz);

        }

    }

}
