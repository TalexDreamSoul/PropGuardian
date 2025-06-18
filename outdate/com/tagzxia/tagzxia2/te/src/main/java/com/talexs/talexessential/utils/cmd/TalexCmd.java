package com.tagzxia2.te.src.main.java.com.talexs.talexessential.utils.cmd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TalexCmd {

    String value();

    boolean IgnoreCase() default true;

    String permission() default "";

    CmdSender cmdSender() default CmdSender.BOTH;

    enum CmdSender {
        CONSOLE, PLAYER, BOTH
    }

}
