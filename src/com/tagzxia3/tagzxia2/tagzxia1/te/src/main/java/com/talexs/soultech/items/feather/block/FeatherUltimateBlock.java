package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.feather.block;

import com.talexs.soultech.classifies.MaterialClass;
import com.talexs.soultech.internal.entity.items.ItemClass;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.items.feather.BaseFeatherItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class FeatherUltimateBlock extends BaseFeatherItem {
    public FeatherUltimateBlock() {
        super("feather_ultimate_block", new ItemBuilder(api().getItemHead("53277"))
                .setName("&f轻辉终级方块")
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired(new MineCraftItem(Material.NETHERITE_INGOT))
                .addRequired(new MineCraftItem(Material.ENDER_PEARL))
                .addRequired(new MineCraftItem(Material.NETHERITE_INGOT))
                .addRequired(new MineCraftItem(Material.ENDER_PEARL))
                .addRequired("feather_advanced_block")
                .addRequired(new MineCraftItem(Material.ENDER_PEARL))
                .addRequired(new MineCraftItem(Material.NETHERITE_INGOT))
                .addRequired(new MineCraftItem(Material.ENDER_PEARL))
                .addRequired(new MineCraftItem(Material.NETHERITE_INGOT))
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(10));
    }

}
