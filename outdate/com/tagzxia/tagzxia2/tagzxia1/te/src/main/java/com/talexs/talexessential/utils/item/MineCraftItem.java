package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.item;

import com.talexs.soultech.internal.entity.items.TalexItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MineCraftItem extends TalexItem {

    public MineCraftItem(Material material) {

        super(new ItemStack(material));

        addIgnoreType(VerifyIgnoreTypes.MINECRAFT_CHECKER);

    }

}
