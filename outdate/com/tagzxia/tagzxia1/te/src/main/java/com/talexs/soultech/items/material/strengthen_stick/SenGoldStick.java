package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.items.material.strengthen_stick;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class SenGoldStick extends BaseMaterialItem {
    public SenGoldStick() {
        super("st_strengthen_gold_stick", new ItemBuilder(Material.STICK).setName("&f强化木棍 (金)").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_strengthen_gold_stick", this)
                .addRequiredNull()
                .addRequired("st_strengthen_iron_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_iron_stick")
                .addRequired(new MineCraftItem(Material.GOLD_BLOCK))
                .addRequired("st_strengthen_iron_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_iron_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(7));
    }

}
