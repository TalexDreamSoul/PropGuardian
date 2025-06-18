package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.material;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class SteelGlass extends BaseMaterialItem {
    public SteelGlass() {
        super("st_steel_glass", new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS).setName("&f钢化玻璃")
//                .setLore("", "&8| &e不会被苦力怕破坏", "")
                .toItemStack());
    }

    public RecipeObject getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .setAmount(4)
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.GLASS))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.GLASS))
                .addRequired(SoulTechItem.get("st_steel_ingot"))
                .addRequired(new MineCraftItem(Material.GLASS))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.GLASS))
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(5));
    }

}
