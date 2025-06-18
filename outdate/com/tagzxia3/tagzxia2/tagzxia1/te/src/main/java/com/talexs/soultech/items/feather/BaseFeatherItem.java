package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.feather;

import com.talexs.soultech.classifies.FeatherClass;
import com.talexs.soultech.classifies.MachineClass;
import com.talexs.soultech.internal.entity.items.IHasUnlockConditions;
import com.talexs.soultech.internal.entity.items.INeedPrepositions;
import com.talexs.soultech.internal.entity.items.ItemClass;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.preposition.IPreposition;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@ItemClass(FeatherClass.class)
public abstract class BaseFeatherItem extends SoulTechItem implements IHasUnlockConditions, INeedPrepositions {
    public BaseFeatherItem(@NotNull String ID, @NotNull ItemStack stack) {
        super(ID, stack);
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return null;
    }

    @Override
    public Set<IPreposition> getPreposition() {
        return null;
    }

}
