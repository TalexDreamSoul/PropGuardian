package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.feather;

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

public class FeatherCore extends BaseFeatherItem {
    public FeatherCore() {
        super("feather_core", new ItemBuilder(api().getItemHead("33435"))
                .setName("&c轻辉核心")
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired("fc_advanced_block")
                .addRequired("fc_ultimate_block")
                .addRequired("fc_advanced_block")
                .addRequired("fc_ultimate_block")
                .addRequired("feather_alloy_block")
                .addRequired("fc_ultimate_block")
                .addRequired("fc_advanced_block")
                .addRequired("fc_ultimate_block")
                .addRequired("fc_advanced_block")
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(15));
    }

}
