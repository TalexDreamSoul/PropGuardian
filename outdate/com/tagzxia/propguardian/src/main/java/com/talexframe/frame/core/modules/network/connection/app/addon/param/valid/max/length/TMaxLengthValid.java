package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.max.length;

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
public @interface TMaxLengthValid {

    /**
     * 校验规则 # 必须满足小于等于长度
     */
    int value();

    /**
     * 校验失败提示信息
     */
    String msg() default "不满足最大长度区间!";

}
