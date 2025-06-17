package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.core.IPlugin;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.core.PluginInfo;
import com.talexframe.frame.core.pojo.mapper.frame.FramePluginSender;
import com.talexframe.frame.core.talex.FrameCreator;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Getter
public abstract class WebPlugin extends FrameCreator implements IPlugin {

    protected final TFrame tframe = TFrame.tframe;
    protected final JarFile jarFile;
    protected final File pluginDataFolder;
    protected final File tempDataFolder;
    private final String name;

    protected FramePluginSender consoleSender = new FramePluginSender(this);

    @Setter
    private PluginInfo info;

    public WebPlugin(String pluginName) {

        super("PLUGIN", pluginName);

        this.name = pluginName;

        this.jarFile = tframe.getPluginManager().getPluginJarFiles().get(this.getName());

        this.pluginDataFolder = new File(tframe.getPluginManager().getPluginFolder() + "/" + pluginName);
        this.tempDataFolder = new File(this.pluginDataFolder + "/temp");

        if ( !this.pluginDataFolder.exists() ) {

            this.pluginDataFolder.mkdirs();

        }

        if ( !this.tempDataFolder.exists() ) {

            this.tempDataFolder.mkdirs();

        }

    }

    @SneakyThrows
    protected void output(String path) {

        String reference = pluginDataFolder + "/";
        FileUtil.mkdir(reference);

        Enumeration<JarEntry> entries = jarFile.entries();

        for ( JarEntry entry = entries.nextElement(); entries.hasMoreElements(); entry = entries.nextElement() ) {

            if ( entry.getName().startsWith(path) ) {

                if ( entry.isDirectory() ) {

                    File file = new File(reference + "/" + entry.getName());

                    if ( !file.exists() ) {

                        file.mkdirs();

                        tframe.getFrameSender().sendConsoleMessage("[PluginInfo] [" + getName() + "] 已创建文件夹: " + entry.getName());

                    }

                    continue;

                }

                File target = new File(reference + "/" + entry.getName());

                if ( target.exists() ) {
                    continue;
                }

                FileOutputStream os = new FileOutputStream(target);

                IoUtil.write(os, true, IoUtil.readBytes(jarFile.getInputStream(entry)));

                tframe.getFrameSender().sendConsoleMessage("[PluginInfo] [" + getName() + "] 载入文件: " + entry.getName());

            }

        }

    }

}
