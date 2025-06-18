package com.tagzxia3.te.src.main.java.com.talexs.soultech.items.feather.block_compress;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.feather.block_compress.BaseFeatherCompressedItem;
import com.talexs.soultech.classifies.MaterialClass;
import com.talexs.soultech.internal.entity.items.ItemClass;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

public class FCAdvancedBlock extends BaseFeatherCompressedItem {
    public FCAdvancedBlock() {
        super("fc_advanced_block", new ItemBuilder(api().getItemHead("41499"))
                .setName("&f轻辉高级压缩方块")
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired(new MineCraftItem(Material.AMETHYST_BLOCK))
                .addRequired(new MineCraftItem(Material.CALCITE))
                .addRequired(new MineCraftItem(Material.AMETHYST_BLOCK))
                .addRequired(new MineCraftItem(Material.CALCITE))
                .addRequired("feather_advanced_block")
                .addRequired(new MineCraftItem(Material.CALCITE))
                .addRequired(new MineCraftItem(Material.AMETHYST_BLOCK))
                .addRequired(new MineCraftItem(Material.CALCITE))
                .addRequired(new MineCraftItem(Material.AMETHYST_BLOCK))
                ;
    }

}
