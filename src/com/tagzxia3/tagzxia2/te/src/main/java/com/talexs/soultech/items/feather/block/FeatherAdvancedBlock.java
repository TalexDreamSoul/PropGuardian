package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.items.feather.block;

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

public class FeatherAdvancedBlock extends BaseFeatherItem {
    public FeatherAdvancedBlock() {
        super("feather_advanced_block", new ItemBuilder(api().getItemHead("53278"))
                .setName("&f轻辉高级方块")
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired(new MineCraftItem(Material.AMETHYST_SHARD))
                .addRequired(new MineCraftItem(Material.SNOW_BLOCK))
                .addRequired(new MineCraftItem(Material.AMETHYST_SHARD))
                .addRequired(new MineCraftItem(Material.SNOW_BLOCK))
                .addRequired("feather_primary_block")
                .addRequired(new MineCraftItem(Material.SNOW_BLOCK))
                .addRequired(new MineCraftItem(Material.AMETHYST_SHARD))
                .addRequired(new MineCraftItem(Material.SNOW_BLOCK))
                .addRequired(new MineCraftItem(Material.AMETHYST_SHARD))
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(10));
    }

}
