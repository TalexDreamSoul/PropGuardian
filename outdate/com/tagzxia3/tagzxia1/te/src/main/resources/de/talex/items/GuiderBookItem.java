package com.tagzxia3.tagzxia1.te.src.main.resources.de.talex.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.TalexItem;

public class GuiderBookItem {

    public GuiderBookItem(PlayerData playerData) {

        if ( playerData.isFirstUse() ) {
            return;
        }

        new TalexItem(new ItemBuilder(Material.BOOK).setName("§b§l◈ §5灵魂科技 §e向导书").setLore("", "§8> §f恐惧源于未知...", "").addEnchant(Enchantment.DURABILITY, 1).addFlag(ItemFlag.HIDE_ENCHANTS)).setType("guider").setType("st_items")
                .addToPlayer(playerData.getPlayer());

    }

}
