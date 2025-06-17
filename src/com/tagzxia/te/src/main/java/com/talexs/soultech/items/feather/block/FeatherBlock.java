package com.tagzxia.te.src.main.java.com.talexs.soultech.items.feather.block;

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

public class FeatherBlock extends BaseFeatherItem {
    public FeatherBlock() {
        super("feather_block", new ItemBuilder(api().getItemHead("33436"))
                .setName("&f轻辉方块")
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired(new MineCraftItem(Material.COPPER_INGOT))
                .addRequired(new MineCraftItem(Material.IRON_INGOT))
                .addRequired(new MineCraftItem(Material.COPPER_INGOT))
                .addRequired(new MineCraftItem(Material.GOLD_INGOT))
                .addRequired(new MineCraftItem(Material.FEATHER))
                .addRequired(new MineCraftItem(Material.GOLD_INGOT))
                .addRequired(new MineCraftItem(Material.COPPER_INGOT))
                .addRequired(new MineCraftItem(Material.IRON_INGOT))
                .addRequired(new MineCraftItem(Material.COPPER_INGOT))
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(5));
    }
}
