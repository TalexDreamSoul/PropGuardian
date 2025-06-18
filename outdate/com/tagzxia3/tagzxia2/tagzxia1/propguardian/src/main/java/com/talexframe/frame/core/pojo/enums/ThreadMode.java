package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.enums;

/**
 * @author TalexDreamSoul
 */

public enum ThreadMode {

    /**
     * 注册与发布在同一个线程，消除了线程切换
     */
    POSTING,

    /**
     * 网络访问等耗时操作，完成后异步通知
     */
    ASYNC

}
