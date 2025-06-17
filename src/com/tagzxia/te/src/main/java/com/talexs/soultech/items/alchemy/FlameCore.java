package com.tagzxia.te.src.main.java.com.talexs.soultech.items.alchemy;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class FlameCore extends BaseAlchemyItem {
    public FlameCore() {
        super("st_flame_core", new ItemBuilder(Material.FIRE_CHARGE).setName("&e烈焰核心").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_harden_core", this)
                .addRequired(new MineCraftItem(Material.NETHERRACK))
                .addRequired(new MineCraftItem(Material.COAL_BLOCK))
                .addRequired(new MineCraftItem(Material.NETHERRACK))
                .addRequired(new MineCraftItem(Material.COAL_BLOCK))
                .addRequired("st_compress_flint_1")
                .addRequired(new MineCraftItem(Material.COAL_BLOCK))
                .addRequired(new MineCraftItem(Material.NETHERRACK))
                .addRequired(new MineCraftItem(Material.COAL_BLOCK))
                .addRequired(new MineCraftItem(Material.NETHERRACK))
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(20));
    }
}
