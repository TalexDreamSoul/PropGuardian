package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.items.material;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class SteelEmbryo extends BaseMaterialItem {
    public SteelEmbryo() {
        super("st_steel_embryo", new ItemBuilder(Material.CLAY_BALL).setName("&f钢锭胚胎").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_steel_embryo", this)
                .addRequired(new MineCraftItem(Material.COAL))
                .addRequired(new MineCraftItem(Material.IRON_INGOT))
                .addRequired(new MineCraftItem(Material.COAL))
                .addRequired(new MineCraftItem(Material.IRON_INGOT))
                .addRequired("st_sticky_string")
                .addRequired(new MineCraftItem(Material.IRON_INGOT))
                .addRequired(new MineCraftItem(Material.COAL))
                .addRequired(new MineCraftItem(Material.IRON_INGOT))
                .addRequired(new MineCraftItem(Material.COAL))
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(5));
    }

}
