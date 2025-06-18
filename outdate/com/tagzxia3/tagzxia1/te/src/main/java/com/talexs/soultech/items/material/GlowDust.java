package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.material;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class GlowDust extends BaseMaterialItem {
    public GlowDust() {
        super("st_glow_dust", new ItemBuilder(Material.GLOWSTONE_DUST).setName("&e荧光粉尘").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_glow_dust", this)
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.GLOWSTONE_DUST))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.GLOWSTONE_DUST))
                .addRequired(new MineCraftItem(Material.GLOWSTONE))
                .addRequired(new MineCraftItem(Material.GLOWSTONE_DUST))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.GLOWSTONE_DUST))
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(5));
    }
}
