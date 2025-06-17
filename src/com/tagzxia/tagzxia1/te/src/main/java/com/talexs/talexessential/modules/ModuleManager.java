package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.BaseModule;
import com.talexs.talexessential.TalexEssential;
import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.PluginClassLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.jar.JarFile;

/**
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/09/03 下午 02:29
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class ModuleManager {

    public static ModuleManager INS = new ModuleManager();

    public static Map<Class<? extends com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.BaseModule>, com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.BaseModule> mapper = new HashMap<>();

    private ModuleManager() {

    }

    @SneakyThrows
    public void scanFor(JavaPlugin plugin, @NotNull String _pos) throws NoSuchFieldException, IllegalAccessException {
        String pos = _pos.replace(".", "/");

        ClassLoader classLoader = plugin.getClass().getClassLoader();
        PluginClassLoader configuredPluginClassLoader = (PluginClassLoader) classLoader;
        Class<? extends PluginClassLoader> aClass = configuredPluginClassLoader.getClass();

        Field fileField = aClass.getDeclaredField("jar");
        fileField.setAccessible(true);
        JarFile file = (JarFile) fileField.get(configuredPluginClassLoader);

        file.stream().forEach(jarEntry -> {
            String name = jarEntry.getName();
            if (!name.startsWith(pos)) {
                TalexEssential.getInstance().getLogger().finer("[ModuleManager] The class " + name + " is not in the package " + pos);
                return;
            }
            if (!name.endsWith(".class")) {
                TalexEssential.getInstance().getLogger().finer("[ModuleManager] The class " + name + " is not a class file.");
                return;
            }
            if ( name.endsWith("BaseModule.class") ) return;

            // load jarEntry
            String className = name.replace("/", ".").substring(0, name.length() - 6);
            try {

                Class<?> targetClz = configuredPluginClassLoader.loadClass(className);

                if (!com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.BaseModule.class.isAssignableFrom(targetClz)) {
                    return;
                }

                TalexEssential.getInstance().getLogger().finer("[ModuleManager] The class " + name + " is assignable to Module.");

                com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.BaseModule bm = (com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.BaseModule) targetClz.newInstance();

                String moduleName = bm.name;

                boolean aBoolean = TalexEssential.getInstance().getConfig().getBoolean("Modules." + moduleName + ".enable", true);

                if ( aBoolean )
                    bm._internalInit();
                else return;

                TalexEssential.getInstance().getLogger().info("[Module] " + moduleName + " automatically load!");

                bm.onEnable();

                TalexEssential.getInstance().getLogger().info("[Module] " + moduleName + " enabled!");

                mapper.put(bm.getClass(), bm);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

    }

    public void allModulesDone() {
        mapper.values().forEach(com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.BaseModule::onAllModulesEnabled);
    }

    public void disableAll() {
        mapper.values().forEach(BaseModule::onDisable);
    }

}
