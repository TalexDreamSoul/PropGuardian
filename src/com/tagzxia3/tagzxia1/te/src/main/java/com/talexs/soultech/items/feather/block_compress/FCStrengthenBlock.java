package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.feather.block_compress;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.feather.block_compress.BaseFeatherCompressedItem;
import com.talexs.soultech.classifies.MaterialClass;
import com.talexs.soultech.internal.entity.items.ItemClass;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

public class FCStrengthenBlock extends BaseFeatherCompressedItem {
    public FCStrengthenBlock() {
        super("fc_strengthen_block", new ItemBuilder(api().getItemHead("34432"))
                .setName("&f轻辉强化压缩方块")
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired(new MineCraftItem(Material.REDSTONE_BLOCK))
                .addRequired(new MineCraftItem(Material.DIAMOND_BLOCK))
                .addRequired(new MineCraftItem(Material.REDSTONE_BLOCK))
                .addRequired(new MineCraftItem(Material.DIAMOND_BLOCK))
                .addRequired("feather_strengthen_block")
                .addRequired(new MineCraftItem(Material.DIAMOND_BLOCK))
                .addRequired(new MineCraftItem(Material.REDSTONE_BLOCK))
                .addRequired(new MineCraftItem(Material.DIAMOND_BLOCK))
                .addRequired(new MineCraftItem(Material.REDSTONE_BLOCK))
                ;
    }

}
