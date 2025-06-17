package com.tagzxia3.te.src.main.java.com.talexs.soultech.items.soul;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.soul.BaseSoulItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Soul extends BaseSoulItem {
    public Soul() {
        super("st_soul", new ItemBuilder(Material.IRON_NUGGET)
                .setName("&d奇异魂魄")
                .addEnchant(Enchantment.ARROW_DAMAGE, 1)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .toItemStack());
    }
}
