package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.material;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class SteelIngot extends BaseMaterialItem {
    public SteelIngot() {
        super("st_steel_ingot", new ItemBuilder(api().getItemHead("50095")).setName("&f粗制钢锭").toItemStack());
    }

    public FurnaceCauldronRecipe getRecipe() {
        return new FurnaceCauldronRecipe(getID(), this, 300)
                .setAmount(4)
                .setNeed(SoulTechItem.get("st_steel_embryo"))
                .setExport(this);
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(5));
    }

}
