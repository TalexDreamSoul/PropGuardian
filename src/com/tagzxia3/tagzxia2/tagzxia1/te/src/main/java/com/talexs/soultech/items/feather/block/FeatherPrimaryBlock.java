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

public class FeatherPrimaryBlock extends BaseFeatherItem {
    public FeatherPrimaryBlock() {
        super("feather_primary_block", new ItemBuilder(api().getItemHead("53279"))
                .setName("&f轻辉初级方块")
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired(new MineCraftItem(Material.LAPIS_LAZULI))
                .addRequired(new MineCraftItem(Material.EMERALD))
                .addRequired(new MineCraftItem(Material.LAPIS_LAZULI))
                .addRequired(new MineCraftItem(Material.EMERALD))
                .addRequired("feather_strengthen_block")
                .addRequired(new MineCraftItem(Material.EMERALD))
                .addRequired(new MineCraftItem(Material.LAPIS_LAZULI))
                .addRequired(new MineCraftItem(Material.EMERALD))
                .addRequired(new MineCraftItem(Material.LAPIS_LAZULI))
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(10));
    }

}
