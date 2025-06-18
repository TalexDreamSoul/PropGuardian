package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.app;

import com.talexframe.frame.core.modules.application.TApp;
import com.talexframe.frame.core.modules.event.service.Cancellable;
import com.talexframe.frame.core.modules.event.service.TalexEvent;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import lombok.Getter;

/**
 * {@link com.talexframe.frame.core.modules.event.events.app Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 01:10 Project: TalexFrame
 */
@Getter
public class AppPreRegisterEvent extends TalexEvent implements Cancellable {

    private final WebPlugin plugin;
    private final TApp controller;

    public AppPreRegisterEvent(WebPlugin plugin, TApp controller) {

        this.plugin = plugin;
        this.controller = controller;

    }

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {

        return cancelled;
    }

    @Override
    public void setCancelled(boolean var) {

        this.cancelled = var;

    }

}
