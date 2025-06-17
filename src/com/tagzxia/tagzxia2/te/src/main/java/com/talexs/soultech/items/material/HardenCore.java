package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.material;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class HardenCore extends BaseMaterialItem {
    public HardenCore() {
        super("st_harden_core", new ItemBuilder(Material.TORCH).setName("&e硬质核心 (木)").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_harden_core", this)
                .addRequiredNull()
                .addRequired("st_glow_dust")
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.STONE))
                .addRequired("compress_wood_1")
                .addRequired(new MineCraftItem(Material.STONE))
                .addRequiredNull()
                .addRequired("compress_wood_1")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(2));
    }
}
