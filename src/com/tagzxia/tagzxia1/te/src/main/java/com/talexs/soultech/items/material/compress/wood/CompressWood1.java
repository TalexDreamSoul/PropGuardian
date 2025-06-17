package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.items.material.compress.wood;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.items.material.compress.BaseCompress;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class CompressWood1 extends BaseCompress {

    public CompressWood1() {

        super("wood_1", new ItemBuilder(Material.OAK_PLANKS).setName("§f压缩木板 §8(x9)").addFlag(ItemFlag.HIDE_ENCHANTS).addEnchant(Enchantment.DURABILITY, 1).toItemStack(),


                new ItemBuilder(Material.OAK_PLANKS).toItemStack()
        );

        addIgnoreType(SoulTechItem.VerifyIgnoreTypes.IgnoreAmount)
//                .addIgnoreType(VerifyIgnoreTypes.SUFFIX_MATCHER)
                .addIgnoreType(VerifyIgnoreTypes.IgnoreDisplayName)
                .addIgnoreType(SoulTechItem.VerifyIgnoreTypes.IgnoreDurability);
    }

    @Override
    public WorkBenchRecipe getRecipe() {

        WorkBenchRecipe wbr = new WorkBenchRecipe("stc_compress_wood_1", this);

        wbr.addRequired(new MineCraftItem(Material.OAK_PLANKS));
        wbr.addRequired(new MineCraftItem(Material.OAK_PLANKS));
        wbr.addRequired(new MineCraftItem(Material.OAK_PLANKS));
        wbr.addRequired(new MineCraftItem(Material.OAK_PLANKS));
        wbr.addRequired(new MineCraftItem(Material.OAK_PLANKS));
        wbr.addRequired(new MineCraftItem(Material.OAK_PLANKS));
        wbr.addRequired(new MineCraftItem(Material.OAK_PLANKS));
        wbr.addRequired(new MineCraftItem(Material.OAK_PLANKS));
        wbr.addRequired(new MineCraftItem(Material.OAK_PLANKS));

        return wbr;

    }

}
