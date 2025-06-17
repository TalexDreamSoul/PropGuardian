package com.tagzxia.te.src.main.java.com.talexs.soultech.items.machine;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;

import java.util.Set;

public class RoughMesh extends BaseMachineItem {
    public RoughMesh() {
        super("st_rough_mesh", new ItemBuilder(Material.RAIL).setName("&f粗制筛网元件").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_rough_mesh", this)
                .addRequired("st_sticky_string")
                .addRequiredNull()
                .addRequired("st_sticky_string")
                .addRequired("st_sticky_string")
                .addRequired("st_strengthen_iron_stick")
                .addRequired("st_sticky_string")
                .addRequired("st_sticky_string")
                .addRequiredNull()
                .addRequired("st_sticky_string")
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(10));
    }

}
