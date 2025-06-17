package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.base;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.base.BaseMachineRecipe;
import com.talexs.soultech.classifies.AlchemyClass;
import com.talexs.soultech.classifies.BaseClass;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.soultech.internal.entity.items.IHasUnlockConditions;
import com.talexs.soultech.internal.entity.items.INeedPrepositions;
import com.talexs.soultech.internal.entity.items.ItemClass;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.preposition.IPreposition;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@ItemClass(BaseClass.class)
public abstract class BaseItem extends SoulTechItem implements IHasUnlockConditions, INeedPrepositions {
    public BaseItem(@NotNull String ID, @NotNull ItemStack stack) {
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

    @Override
    public RecipeObject getRecipe() {
        return new BaseMachineRecipe(this);
    }
}
