package com.tagzxia3.propguardian.src.main.java.com.talexframe.frame.core.pojo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command标识 方法必须要有默认的 ISender sender 如果为 ISender 实现类，框架会尽可能的匹配 <br /> {@link com.talexframe.frame.pojo.annotations
 * Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 11:48 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD } )
public @interface TCmd {

    /**
     * 监听的子参数 比如命令 /help page 1 如果输入 "page" 就会监听 /help 旗下 page 任意的参数 都会被注入 注入取决于方法参数长度 如果输入/help page 1 方法长度为2 则不匹配
     * 如果为1才匹配 如果可以框架会自动转类型
     * <p>
     * 不填默认监听主参数
     */
    String value() default "";

    /**
     * 是否需要完整匹配 如果是 则如果value填写 hello 就只会检测到/help hello 才会匹配 如果是/help hello 1 不会匹配 本方法不提供任何子参 (只会有默认的 sender)
     */
    boolean completedMatch() default false;

    /**
     * 需要value的大小写匹配
     */
    boolean matchCase() default false;

    /**
     * 忽略内容 - 即 若指令为/help page /help hello 都会触发一个方法长度为1的注入
     */
    boolean ignoreContent() default false;

    /**
     * Null自动补全 例如对于命令/help page 1 如果输入/help page 将自动补充为/help page null补全参数
     */
    boolean nullAutoComplete() default false;

    /**
     * 方法别称 除了value可以匹配，这里的别称也可以匹配
     */
    String[] aliases() default "";

}
