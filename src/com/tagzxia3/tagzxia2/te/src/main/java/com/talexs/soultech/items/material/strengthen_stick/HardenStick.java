package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.items.material.strengthen_stick;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;

import java.util.Set;

public class HardenStick extends BaseMaterialItem {
    public HardenStick() {
        super("st_harden_stick", new ItemBuilder(Material.STICK).setName("&f硬化木棍").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_harden_stick", this)
                .setAmount(4)
                .addRequiredNull()
                .addRequired("compress_wood_1")
                .addRequiredNull()
                .addRequired("compress_wood_1")
                .addRequiredNull()
                .addRequired("compress_wood_1")
                .addRequiredNull();
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(1));
    }

}
