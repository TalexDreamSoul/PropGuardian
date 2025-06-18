package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.method;

import org.springframework.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define a request must be which method sent <br /> {@link com.talexframe.frame.pojo.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 0:45 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface TReqSupportMethod {

    HttpMethod[] value() default { HttpMethod.GET };

}
