package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.items.material;

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

public class SteelFireGlass extends BaseMaterialItem {
    public SteelFireGlass() {
        super("st_steel_fire_glass", new ItemBuilder(Material.GRAY_STAINED_GLASS).setName("&c烧制钢化玻璃")
//                .setLore("", "&8| &e不会被苦力怕破坏", "")
                .toItemStack());
    }

    public FurnaceCauldronRecipe getRecipe() {
        return new FurnaceCauldronRecipe(getID(), this, 600)
                .setNeed(SoulTechItem.get("st_steel_glass"))
                .setExport(this);
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(5));
    }

}
