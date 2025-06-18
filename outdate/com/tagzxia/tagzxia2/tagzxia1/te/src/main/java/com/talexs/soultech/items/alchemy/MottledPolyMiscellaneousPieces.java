package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.alchemy;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.alchemy.BaseAlchemyItem;
import com.talexs.soultech.classifies.MaterialClass;
import com.talexs.soultech.internal.entity.items.ItemClass;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.preposition.IPreposition;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.machine.compressor.CompressorRecipe;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;

import java.util.Set;

public class MottledPolyMiscellaneousPieces extends BaseAlchemyItem {
    public MottledPolyMiscellaneousPieces() {
        super("mottled_poly_miscellaneous_pieces", new ItemBuilder(api().getItemHead("34433"))
                .setName("&b斑驳聚杂块")
                .setLore(
                        "",
                        "&8| &7融合各种矿物金属的特点",
                        "&8| &7聚合而成的斑驳聚杂碎片",
                        "&8| &7适合转化为各类进阶杂块",
                        "",
                        "&8| &7特殊的方法熔炼化为液体",
                        ""
                )
                .toItemStack());
    }

    @Override
    public RecipeObject getRecipe() {
        return new CompressorRecipe(getID(), this).setExport(this)
                .setProcessTime(15 * 60)
                .setTemperature(10000)
                .setPressure(10000)

                .addLoadRequired(Material.NETHER_STAR)
                .addLoadRequired("space_fire_dust")
                .addLoadRequired("gold_hammer_embryo")
                .addLoadRequired("feather_core")

                .addRequired(Material.COPPER_INGOT)
                .addRequired(Material.IRON_INGOT)
                .addRequired(Material.AMETHYST_SHARD)
                .addRequired(Material.IRON_INGOT)
                .addRequired(Material.COPPER_INGOT)
                .addRequired(Material.LAPIS_LAZULI)
                .addRequired(Material.GOLD_INGOT)
                .addRequired(Material.NETHERITE_INGOT)
                .addRequired(Material.GOLD_INGOT)
                .addRequired(Material.LAPIS_LAZULI)
                .addRequired(Material.REDSTONE)
                .addRequired(Material.GOLD_INGOT)
                .addRequired(Material.DIAMOND)
                .addRequired(Material.GOLD_INGOT)
                .addRequired(Material.REDSTONE)
                .addRequired(Material.COPPER_INGOT)
                .addRequired(Material.IRON_INGOT)
                .addRequired(Material.EMERALD)
                .addRequired(Material.IRON_INGOT)
                .addRequired(Material.COPPER_INGOT)
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(100));
    }
}
