package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.application;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.application.TApp;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.application.TAppManager;
import com.talexframe.frame.core.modules.plugins.adapt.PluginCompAdapter;
import com.talexframe.frame.core.modules.plugins.addon.PluginScanner;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.SneakyThrows;

/**
 * {@link com.talexframe.frame.core.modules.application Package }
 *
 * @author TalexDreamSoul 22/04/02 下午 08:44 Project: TalexFrame
 */
public class TAppCompAdapter extends PluginCompAdapter<TApp> {

    private final TAppManager appManager = tframe.getAppManager();

    @Override
    public int getPriority() {

        return 100;
    }

    @SneakyThrows
    @Override
    public boolean injectWithInstance(PluginScanner scanner, WebPlugin webPlugin, TApp instance) {

        return appManager.registerController( webPlugin, instance );

    }

    @Override
    public boolean logoutInstance(PluginScanner scanner, WebPlugin webPlugin, Class<? extends FrameCreator> clazz) {

        return appManager.unRegisterController( webPlugin, appManager.getControllers().get(clazz));

    }

}
