package com.tagzxia3.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.cache.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将结果自动保存到 RedisCache <br /> {@link com.talexframe.frame.core.pojo.dao.factory.redis Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 10:58 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface TRedisCache {

    /**
     * key
     **/
    String value();

    String type() default "frame";

    /**
     * s
     **/
    long expireTime() default -1;

    /**
     * delete mode
     **/
    boolean delete() default false;

}
