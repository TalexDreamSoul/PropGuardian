package com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.trade;

import com.talexs.talexessential.modules.BaseModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TradeModule extends BaseModule {
    public TradeModule() {
        super("trade");
    }

    @Override
    public void onEnable() {

    }

    @Override
    protected boolean configurable() {
        return false;
    }
}
