package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.items.material;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class StickyString extends BaseMaterialItem {
    public StickyString() {
        super("st_sticky_string", new ItemBuilder(Material.STRING).setName("&f粘性丝").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_sticky_string", this)
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.FEATHER))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.STRING))
                .addRequired(new MineCraftItem(Material.SLIME_BALL))
                .addRequired(new MineCraftItem(Material.STRING))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.STRING))
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(5));
    }

}
