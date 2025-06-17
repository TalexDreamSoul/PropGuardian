package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.core;

public interface IPlugin {

    /** 当插件即将被扫包时调用 **/
    void onPreScan();

    /**
     * 当插件被启用时调用 （此时扫包已完成） onScanned()
     **/
    void onScanned();

    /**
     * 当插件被卸载时调用
     **/
    void onDisable();

}
