package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.event.service;

/**
 * 标志者一个事件可以被取消
 */
public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean var);

}
