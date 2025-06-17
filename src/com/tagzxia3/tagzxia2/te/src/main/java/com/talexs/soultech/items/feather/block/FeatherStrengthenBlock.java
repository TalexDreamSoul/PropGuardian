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

public class FeatherStrengthenBlock extends BaseFeatherItem {
    public FeatherStrengthenBlock() {
        super("feather_strengthen_block", new ItemBuilder(api().getItemHead("22460"))
                .setName("&f轻辉强化方块")
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired(new MineCraftItem(Material.REDSTONE))
                .addRequired(new MineCraftItem(Material.DIAMOND))
                .addRequired(new MineCraftItem(Material.REDSTONE))
                .addRequired(new MineCraftItem(Material.DIAMOND))
                .addRequired("feather_block")
                .addRequired(new MineCraftItem(Material.DIAMOND))
                .addRequired(new MineCraftItem(Material.REDSTONE))
                .addRequired(new MineCraftItem(Material.DIAMOND))
                .addRequired(new MineCraftItem(Material.REDSTONE))
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(10));
    }

}
