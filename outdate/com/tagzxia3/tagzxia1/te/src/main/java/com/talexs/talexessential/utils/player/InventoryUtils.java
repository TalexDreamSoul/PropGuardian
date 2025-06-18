package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

public class InventoryUtils {

    public static int deletePlayerItem(Player player, Material material, int i) {
        PlayerInventory inventory = player.getInventory();

        int slot = -1, reduces = 0;

        while ( (slot = inventory.first(material)) != -1 ) {

            ItemStack item = inventory.getItem(slot);

            if ( item.getAmount() > i ) {
                item.setAmount(item.getAmount() - i);
                reduces += i;
                break;
            } else {
                i = i - item.getAmount();
                reduces += item.getAmount();
                inventory.setItem(slot, null);
            }

        }

        return reduces;
    }

    public static Integer getPlayerItemInInventory(Player player, Material material) {

        int i = 0;
        for ( ItemStack is : player.getInventory().getContents() ) {
            if ( is != null && is.getType() == material ) {
                i = i + is.getAmount();
            }
        }
        return i;
    }

}
