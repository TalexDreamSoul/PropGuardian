package com.tagzxia.te.src.main.java.com.talexs.soultech.items.material.compress.wood;

import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.items.material.compress.BaseCompress;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class CompressWood2 extends BaseCompress {

    public CompressWood2() {

        super("wood_2", new ItemBuilder(Material.OAK_PLANKS).setName("§f压缩木板 §8(x81)").addFlag(ItemFlag.HIDE_ENCHANTS).addEnchant(Enchantment.DURABILITY, 1).toItemStack(),


                new ItemBuilder(Material.OAK_PLANKS).toItemStack()
        );

        addIgnoreType(VerifyIgnoreTypes.IgnoreAmount)
//                .addIgnoreType(VerifyIgnoreTypes.SUFFIX_MATCHER)
                .addIgnoreType(VerifyIgnoreTypes.IgnoreDisplayName)
                .addIgnoreType(VerifyIgnoreTypes.IgnoreDurability);

    }

    @Override
    public WorkBenchRecipe getRecipe() {

        WorkBenchRecipe wbr = new WorkBenchRecipe("stc_compress_wood_2", this);

        wbr.addRequired("compress_wood_1");
        wbr.addRequired("compress_wood_1");
        wbr.addRequired("compress_wood_1");
        wbr.addRequired("compress_wood_1");
        wbr.addRequired("compress_wood_1");
        wbr.addRequired("compress_wood_1");
        wbr.addRequired("compress_wood_1");
        wbr.addRequired("compress_wood_1");
        wbr.addRequired("compress_wood_1");

        return wbr;

    }

}
