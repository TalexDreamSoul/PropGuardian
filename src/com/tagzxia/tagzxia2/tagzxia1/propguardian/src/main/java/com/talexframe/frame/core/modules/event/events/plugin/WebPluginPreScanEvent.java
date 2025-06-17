package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.plugin;

import com.talexframe.frame.core.modules.event.service.TalexEvent;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import lombok.Getter;

/**
 * {@link com.talexframe.frame.core.modules.event.events.plugin Package }
 *
 * @author TalexDreamSoul 22/08/01 下午 02:58 Project: TalexFrame
 */
public class WebPluginPreScanEvent extends TalexEvent {

    @Getter
    private final WebPlugin plugin;

    public WebPluginPreScanEvent(WebPlugin plugin) {

        this.plugin = plugin;

    }

}
