package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.service;

import com.talexframe.frame.core.pojo.enums.EventPriority;
import com.talexframe.frame.core.pojo.enums.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author TalexDreamSoul
 */
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
public @interface THandler {

    ThreadMode threadMode() default ThreadMode.POSTING;

    /**
     * 事件优先级
     */
    EventPriority priority() default EventPriority.NORMAL;

    /**
     * 是否只监听一次
     */
    boolean once() default false;

}
