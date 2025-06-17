package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.alchemy;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.alchemy.BaseAlchemyItem;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.alchemy.FlameCore;
import com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;

import java.util.Set;

public class FireFlameCore extends BaseAlchemyItem {

    public FireFlameCore() {
        super("st_fire_flame_core", new ItemBuilder(Material.MAGMA_CREAM).setName("&e灼热烈焰核心").toItemStack());
    }

    public FurnaceCauldronRecipe getRecipe() {
        return new FurnaceCauldronRecipe("fcr_fire_flame_core",
                    this, 900
                )
                .setAmount(2)
                .setNeed(SoulTechItem.get("st_flame_core"))
                .setExport(this)
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(30));
    }
}
