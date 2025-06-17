package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.repository;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.repository.TRepo;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.repository.TRepoManager;
import com.talexframe.frame.core.modules.plugins.adapt.PluginCompAdapter;
import com.talexframe.frame.core.modules.plugins.addon.PluginScanner;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * {@link com.talexframe.frame.core.modules.application Package }
 *
 * @author TalexDreamSoul 22/04/02 下午 08:44 Project: TalexFrame
 */
@Slf4j
public class TRepoCompAdapter extends PluginCompAdapter<TRepo> {

    private final TRepoManager repoManager = tframe.getRepoManager();

    @SneakyThrows
    @Override
    public boolean injectWithInstance(PluginScanner scanner, WebPlugin webPlugin, TRepo instance) {

        log.debug("[TRepoCompAdapter] injectWithInstance: {}", instance.getClass().getName());

        return repoManager.registerRepo( webPlugin, instance );

    }

    @Override
    public boolean logoutInstance(PluginScanner scanner, WebPlugin webPlugin, Class<? extends FrameCreator> clazz) {

        log.info("[Repository] Unregister owned by {} | {}", webPlugin.getName(), clazz.getName());

        return repoManager.unRegisterRepo( webPlugin, repoManager.getRepoByClass(clazz));

    }

    @Override
    public Object createInstance(WebPlugin webPlugin, Class<?> cls, Constructor<?>[] constructors) throws InvocationTargetException, InstantiationException, IllegalAccessException {

        Constructor<?> constructor = constructors[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        if( parameterTypes.length > 0 && parameterTypes[0] == WebPlugin.class ) {

            return constructor.newInstance(webPlugin);

        }

        return super.createInstance(webPlugin, cls, constructors);
    }

}
