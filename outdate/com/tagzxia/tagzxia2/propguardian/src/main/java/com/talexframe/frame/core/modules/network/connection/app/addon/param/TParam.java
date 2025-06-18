package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.param;

import com.google.common.annotations.Beta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注入JSON字段 <br /> {@link com.talexframe.frame.pojo.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 20:33 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.PARAMETER, ElementType.TYPE, ElementType.FIELD } )
public @interface TParam {

    /**
     * 是否允许字段不存在 如果不存在且此项为假则会报错并且返回友好提示
     */
    boolean nullable() default false;

    /**
     * 当字段不存在时 将自动根据参数类型将输入内容转换输入
     * 如果输入类型不匹配将会传入 null
     */
    @Beta
    String defaultValue() default "";

    /**
     * 默认解析json字段为当前参数名，设置后将按照field解析
     */
    String value() default "";

}
