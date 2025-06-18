package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.tools.pickaxe;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.items.tools.BaseToolItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;

import java.util.Set;

public class HardPickaxe extends BaseToolItem {
    public HardPickaxe() {
        super("st_hard_pickaxe", new ItemBuilder(Material.WOODEN_PICKAXE).setName("&f硬化木镐")
                .setUnbreakable().toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_hard_pickaxe", this)
                .addRequired("compress_wood_1")
                .addRequired("st_harden_core")
                .addRequired("compress_wood_1")
                .addRequiredNull()
                .addRequired("st_harden_stick")
                .addRequiredNull()
                .addRequiredNull()
                .addRequired("st_harden_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(5));
    }

}
