package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.items.material.strengthen_stick;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class SenCopperStick extends BaseMaterialItem {
    public SenCopperStick() {
        super("st_strengthen_copper_stick", new ItemBuilder(Material.STICK).setName("&f强化木棍 (铜)").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_strengthen_copper_stick", this)
                .addRequiredNull()
                .addRequired("st_harden_stick")
                .addRequiredNull()
                .addRequired("st_harden_stick")
                .addRequired(new MineCraftItem(Material.COPPER_BLOCK))
                .addRequired("st_harden_stick")
                .addRequiredNull()
                .addRequired("st_harden_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(3));
    }

}
