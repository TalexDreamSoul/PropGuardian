package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.items.material.compress;

import com.talexs.soultech.classifies.MaterialClass;
import com.talexs.soultech.internal.entity.items.ItemClass;
import com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public abstract class BaseCompress extends BaseMaterialItem {

    @Getter
    private ItemStack lastCompress;

    public BaseCompress(String ID, ItemStack stack, ItemStack lastCompress) {

        super("compress_" + ID, stack);

        this.lastCompress = lastCompress;

    }

    @Override
    public void throwItem(PlayerData playerData, PlayerDropItemEvent event) {

        PlayerUser playerUser = new PlayerUser(event.getPlayer());

        if ( event.getPlayer().isSneaking() ) {

            playerUser.delayRun(new PlayerDataRunnable() {

                @Override
                public void run() {

                    Item item = event.getItemDrop();
                    Location loc = item.getLocation();

                    item.remove();

                    loc.getWorld().dropItem(loc, lastCompress);

                }

            }, 5);

        } else {

            playerUser.infoActionBar("蹲下丢出可还原压缩物品!");

        }


    }

}
