package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.feather;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.feather.BaseFeatherItem;
import com.talexs.soultech.classifies.MaterialClass;
import com.talexs.soultech.internal.entity.items.ItemClass;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;

import java.util.Set;

public class FeatherAlloyBlock extends BaseFeatherItem {
    public FeatherAlloyBlock() {
        super("feather_alloy_block", new ItemBuilder(api().getItemHead("33438"))
                .setName("&b轻辉合金方块")
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired("fc_block")
                .addRequired("fc_strengthen_block")
                .addRequired("fc_block")
                .addRequired("fc_strengthen_block")
                .addRequired("fc_primary_block")
                .addRequired("fc_strengthen_block")
                .addRequired("fc_block")
                .addRequired("fc_strengthen_block")
                .addRequired("fc_block")
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(30));
    }

}
