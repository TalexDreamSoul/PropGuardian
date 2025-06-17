package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.luckybag;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.LuckyBagModule;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.talexessential.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public abstract class BaseLuckyBag extends SoulTechItem {

    public BaseLuckyBag(String id, ItemStack itemStack) {
        super(id, itemStack);
    }

    public void drop2Loc(Location loc) {
        loc.getWorld().dropItem(loc, this);
    }

    public void drop2Loc(Location loc, int amount) {
        for (int i = 0; i < amount; ++i ) drop2Loc(loc);
    }

    public abstract void open(Player player);

    @Override
    public boolean onPlaceItem(PlayerData playerData, BlockPlaceEvent event) {
        event.setCancelled(true);

        if ( LuckyBagModule.doEnable(event.getBlock().getWorld()) ) {
            open(playerData.getOfflinePlayer().getPlayer());
        }

        return true;
    }

    public static int getPlayerOpenedAmo(PlayerData pd, String type) {
        return pd.getOptionsSection("luckybag").getInt(type + ".amo", 0);
    }

    public static void setPlayerOpenedAmo(PlayerData pd, String type, int amo) {
        pd.getOptionsSection("luckybag").set(type + ".amo", amo);
    }

    public static void increasePlayerOpenedAmo(PlayerData pd, String type) {
        setPlayerOpenedAmo(pd, type, getPlayerOpenedAmo(pd, type) + 1);
    }

//    public static int getPlayerAllOpened(PlayerData pd) {
//        Set<String> luckybag = pd.getOptionsSection("luckybag").getKeys(false);
//
//        int amo = 0;
//
//        luckybag.forEach(bag -> {
//
//        });
//
//        return amo;
//    }
}
