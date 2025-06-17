package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.addon;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.addon.FramePluginListener;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.addon.PluginScanner;
import com.talexframe.frame.core.modules.event.FrameListener;
import com.talexframe.frame.core.modules.plugins.adapt.PluginCompAdapter;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * {@link com.talexframe.frame.core.modules.application Package }
 *
 * @author TalexDreamSoul 22/04/02 下午 08:44 Project: TalexFrame
 */
@Component
public class TPluginListenerCompAdapter extends PluginCompAdapter<FramePluginListener> {

    @SneakyThrows
    @Override
    public boolean injectWithInstance(PluginScanner scanner, WebPlugin webPlugin, FramePluginListener instance) {

        if( instance.getWebPlugin().getName().equals(webPlugin.getName()) ) {

            tframe.registerListener( instance );

            return true;

        } else return false;

    }

    @Override
    public boolean logoutInstance(PluginScanner scanner, WebPlugin webPlugin, Class<? extends FrameCreator> clazz) {

        for( FrameListener listener : tframe.getListeners().keySet() ) {

            if( listener.getClass().equals(clazz) ) {

                tframe.unRegisterListener((FramePluginListener) listener);

                return true;

            }

        }
        
        return false;

    }

}
