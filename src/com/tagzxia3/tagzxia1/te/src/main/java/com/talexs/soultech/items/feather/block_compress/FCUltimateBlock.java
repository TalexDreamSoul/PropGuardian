package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.feather.block_compress;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.feather.block_compress.BaseFeatherCompressedItem;
import com.talexs.soultech.classifies.MaterialClass;
import com.talexs.soultech.internal.entity.items.ItemClass;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

public class FCUltimateBlock extends BaseFeatherCompressedItem {
    public FCUltimateBlock() {
        super("fc_ultimate_block", new ItemBuilder(api().getItemHead("36108"))
                .setName("&f轻辉终级压缩方块")
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired(new MineCraftItem(Material.NETHERITE_BLOCK))
                .addRequired(new MineCraftItem(Material.WITHER_SKELETON_SKULL))
                .addRequired(new MineCraftItem(Material.NETHERITE_BLOCK))
                .addRequired(new MineCraftItem(Material.WITHER_SKELETON_SKULL))
                .addRequired("feather_ultimate_block")
                .addRequired(new MineCraftItem(Material.WITHER_SKELETON_SKULL))
                .addRequired(new MineCraftItem(Material.NETHERITE_BLOCK))
                .addRequired(new MineCraftItem(Material.WITHER_SKELETON_SKULL))
                .addRequired(new MineCraftItem(Material.NETHERITE_BLOCK))
                ;
    }

}
