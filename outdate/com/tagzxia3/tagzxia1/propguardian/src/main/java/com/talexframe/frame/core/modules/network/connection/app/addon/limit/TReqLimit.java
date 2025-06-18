package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶限流 - 每一个注解都会创建一个令牌桶 慎用 默认 Controller 重写 rate相关方法就可以使用全局限流 如果 Controller类 或 方法 上额外标注 则会在全局令牌桶上叠加 相当于若都放该注解
 * 最后进入方法会经过 三个令牌桶 <br /> {@link com.talexframe.frame.core.modules.network.connection.app.addon.limit Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/23 23:27 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD, ElementType.TYPE } )
public @interface TReqLimit {

    /**
     * 每秒并发 (创建令牌数)
     **/
    double QPS() default 10D;

    /**
     * 获取令牌等待超时时间
     **/
    long timeout() default 100;

    /**
     * 超时时间单位 默认：毫秒
     **/
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}
