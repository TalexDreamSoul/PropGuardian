package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.login;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加了此注解代表需要登录才可以使用相关功能 <br /> {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul
 * @date 2022/6/6 05:00 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD, ElementType.TYPE } )
public @interface TReqLoginChecker {

    /**
     * 这个值默认为true，即需要登录才可以使用相关功能
     * 若为false则代表不登录才能使用相关功能
     */
    boolean value() default true;

}
