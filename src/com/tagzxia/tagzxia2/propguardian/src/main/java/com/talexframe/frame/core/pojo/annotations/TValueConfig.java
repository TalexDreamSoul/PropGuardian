package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加在类上将忽略字段注解进行全类解析
 * {@link com.talexframe.frame.core.pojo.annotations Package }
 *
 * @author TalexDreamSoul 22/04/17 上午 08:34 Project: TalexFrame
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.TYPE } )
public @interface TValueConfig {

    /**
     * 字段名称
     */
    String name() default "";

    /**
     * 允许忽略
     * 是否允许忽略部分字段
     * @return true 允许忽略
     */
    boolean ignore() default false;

    /**
     * 禁止保存
     * 是否禁止保存(部分)字段
     *
     */
    boolean disable() default false;

}
