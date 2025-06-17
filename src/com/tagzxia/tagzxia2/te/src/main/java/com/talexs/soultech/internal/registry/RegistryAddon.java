package com.tagzxia2.te.src.main.java.com.talexs.soultech.internal.registry;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.registry.CategoryRegistry;
import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.registry.IRegistry;
import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.registry.ItemRegistry;
import com.talexs.soultech.addon.BaseAddon;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.utils.LogUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarFile;

@Getter
public class RegistryAddon extends BaseAddon {

    private final Set<com.tagzxia.te.src.main.java.com.talexs.soultech.internal.registry.IRegistry> registries = new HashSet<>();

    @Override
    public void onEnable() {

        registries.add(new CategoryRegistry());
        registries.add(new ItemRegistry());

        // 先把registries从大到小排序，先扫描大的

        List<com.tagzxia.te.src.main.java.com.talexs.soultech.internal.registry.IRegistry> _registries = new ArrayList<>(registries);

        _registries.sort((o1, o2) -> o2.getPriority() - o1.getPriority());

        _registries.forEach(registry -> registry.getScanFolders().forEach(pos -> scanFor(registry, TalexEssential.getInstance(), pos)));

    }

    @SneakyThrows
    public void scanFor(IRegistry registry, JavaPlugin plugin, @NotNull String _pos) {
        String pos = _pos.replace(".", "/");

        ClassLoader classLoader = plugin.getClass().getClassLoader();
        PluginClassLoader configuredPluginClassLoader = (PluginClassLoader) classLoader;
        Class<? extends PluginClassLoader> aClass = configuredPluginClassLoader.getClass();

        Field fileField = aClass.getDeclaredField("jar");
        fileField.setAccessible(true);
        JarFile file = (JarFile) fileField.get(configuredPluginClassLoader);

        AtomicInteger amo = new AtomicInteger();

        file.stream().forEach(jarEntry -> {
            String name = jarEntry.getName();
            if (!name.startsWith(pos)) {
                TalexEssential.getInstance().getLogger().finer("[RegistryAddon] The class " + name + " is not in the package " + pos);
                return;
            }

            if (!name.endsWith(".class")) {
                TalexEssential.getInstance().getLogger().finer("[RegistryAddon] The class " + name + " is not a class file.");
                return;
            }

            String className = name.replace("/", ".").substring(0, name.length() - 6);
            try {

                Class<?> targetClz = configuredPluginClassLoader.loadClass(className);

                if (Modifier.isAbstract(targetClz.getModifiers())) {
                    return;
                }

                Constructor<?> emptyConstructor = getEmptyConstructor(targetClz);

                if ( emptyConstructor == null ) {
//                    LogUtil.log("[RegistryAddon] ATTENTION | Class(" + name + ") does not have empty constructor!");

                    return;
                }

                if (registry.isAssignable(targetClz)) {

                    if ( !registry.register(targetClz) ) {

                        LogUtil.log("&c[RegistryAddon] Class(&e" + name.replace("/", ".") + "&c) register failed!");

                    } else amo.getAndIncrement();

                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        registry.onceScanDone(amo.get());

    }

    public @Nullable Constructor<?> getEmptyConstructor(Class<?> clazz) {

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }

        return null;

    }

}
