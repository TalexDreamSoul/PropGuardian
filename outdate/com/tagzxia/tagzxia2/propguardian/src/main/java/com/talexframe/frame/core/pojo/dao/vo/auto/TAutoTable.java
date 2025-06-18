package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.vo.auto;

import com.google.common.annotations.Beta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link com.talexframe.frame.core.pojo.dao.vo.auto Package }
 *
 * @author TalexDreamSoul 22/03/12 下午 09:44 Project: TalexFrame
 */
@Target( { ElementType.TYPE } )
@Retention( RetentionPolicy.RUNTIME )
public @interface TAutoTable {

    /**
     * 表名字
     * 当作为 SaveManager 时需要提供
     * 作为 TRepoPlus 中的泛型参数时不需要提供
     **/
    @Beta
    String value();

    /**
     * 启用时会自动加入一个 json(info) 信息类存放其他字段 即序列化这个实例类
     **/
    boolean fullJsonRecord() default false;

    /**
     * 精度 如varchar(1024) 则为1024
     **/
    int precision() default 1024;

    /**
     * 启用后会自动加入 as_ 前缀防止sql注入
     **/
    boolean prefix() default false;

}
