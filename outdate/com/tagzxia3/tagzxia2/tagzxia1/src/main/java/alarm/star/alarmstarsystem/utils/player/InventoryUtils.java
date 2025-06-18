package com.tagzxia3.tagzxia2.tagzxia1.src.main.java.alarm.star.alarmstarsystem.utils.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static void deletePlayerItem(Player player, Material material, int i) {

        for ( ItemStack is : player.getInventory().getContents() ) {
            if ( i <= 0 ) {
                break;
            }
            if ( is != null && is.getType() == material ) {
                if ( i >= is.getAmount() ) {
                    i = i - is.getAmount();
                    player.getInventory().removeItem(is);
                } else {
                    is.setAmount(is.getAmount() - i);
                    i = 0;
                }
            }
        }
        player.updateInventory();
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
