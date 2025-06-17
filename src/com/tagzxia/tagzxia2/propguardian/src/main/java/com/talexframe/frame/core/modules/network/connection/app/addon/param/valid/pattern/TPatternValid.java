package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加在字段上进行请求校验
 * {@link com.talexframe.frame.core.pojo.annotations Package }
 *
 * @author TalexDreamSoul 22/04/17 上午 08:34 Project: TalexFrame
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.PARAMETER, ElementType.TYPE } )
public @interface TPatternValid {

    /**
     * 校验规则 # 必须满足当前正则表达式
     */
    String value();

    /**
     * 校验失败提示信息
     */
    String msg() default "";

}
