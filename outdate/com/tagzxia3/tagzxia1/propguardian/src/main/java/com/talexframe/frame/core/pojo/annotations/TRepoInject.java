package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记在 Controller 类中会自动注入字段 <br /> {@link com.talexframe.frame.pojo.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 0:32 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD } )
public @interface TRepoInject {
}
