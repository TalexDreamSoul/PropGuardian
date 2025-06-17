package com.tagzxia.te.src.main.java.com.talexs.soultech.items.material.compress.flint;

import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.items.material.compress.BaseCompress;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CompressFlint extends BaseCompress {
    public CompressFlint() {
        super("flint_1", new ItemBuilder(Material.CHISELED_DEEPSLATE)
                        .setName("&f压缩燧石 &8(&7x9&8)")
                        .toItemStack()
                , new ItemStack(Material.FLINT, 9));

        addIgnoreType(VerifyIgnoreTypes.IgnoreAmount)
//                .addIgnoreType(VerifyIgnoreTypes.SUFFIX_MATCHER)
                .addIgnoreType(VerifyIgnoreTypes.IgnoreDisplayName)
                .addIgnoreType(VerifyIgnoreTypes.IgnoreDurability);
    }

    @Override
    public WorkBenchRecipe getRecipe() {
        WorkBenchRecipe wbr = new WorkBenchRecipe("stc_compress_flint_1", this);

        wbr.addRequired(new MineCraftItem(Material.FLINT));
        wbr.addRequired(new MineCraftItem(Material.FLINT));
        wbr.addRequired(new MineCraftItem(Material.FLINT));
        wbr.addRequired(new MineCraftItem(Material.FLINT));
        wbr.addRequired(new MineCraftItem(Material.FLINT));
        wbr.addRequired(new MineCraftItem(Material.FLINT));
        wbr.addRequired(new MineCraftItem(Material.FLINT));
        wbr.addRequired(new MineCraftItem(Material.FLINT));
        wbr.addRequired(new MineCraftItem(Material.FLINT));

        return wbr;
    }
}
