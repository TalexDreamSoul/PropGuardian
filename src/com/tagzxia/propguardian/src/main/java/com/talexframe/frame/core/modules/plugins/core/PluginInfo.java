package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author TalexDreamSoul
 */
@Getter
@Setter
@Accessors( chain = true )
@AllArgsConstructor
@NoArgsConstructor
public class PluginInfo {

    //    插件名
    private String pluginName;
    //    插件作者
    private String pluginAuthor;
    //    插件版本
    private String pluginVersion;
    //    插件主类
    private String mainClassPath;
    //    插件介绍
    private String description;
    //    插件网页
    private String website;
    //    插件支持的框架版本
    private PluginSupportVersion supportVersion = PluginSupportVersion.SEVEN;

    public enum PluginSupportVersion {

        //        7.0.0
        SEVEN("7.0.0-core"),
        SEVEN_OFFICIAL("7.0.0-core-official");

        @Getter
        private final String version;

        PluginSupportVersion(String version) {

            this.version = version;

        }

    }

}
