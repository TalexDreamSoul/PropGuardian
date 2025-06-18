package com.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.attract;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.PlayerAttractData;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.event.PlayerDoubleSneakEvent;
import com.talexs.talexessential.modules.BaseModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class AttractModule extends BaseModule {

    public AttractModule() {
        super("attract");
    }

    @Override
    public void onEnable() {

    }

    @Override
    protected boolean configurable() {
        return false;
    }

    static TimedCache<String, String> timedCache = CacheUtil.newTimedCache(1000 * 60 * 15);

    static {
        timedCache.schedulePrune(1000 * 60);
    }

    @EventHandler
    public void onDoubleSneak(PlayerDoubleSneakEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = PlayerData.g(player);
        PlayerAttractData pad = pd.getAttractData();

        if ( !pad.isEnable() ) {
            if ( timedCache.get(player.getName(), false) == null ) {
                timedCache.put(player.getName(), player.getName());
                player.sendActionBar("§7手持 §5末影珍珠 §7后 §e蹲下 + 抬头 §7开启 §a物品引波吸呐 §7!");
            }
        } else pad.trigger(player);
    }

}
