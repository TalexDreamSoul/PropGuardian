package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.items.feather.block_compress;

import com.talexs.soultech.classifies.FeatherClass;
import com.talexs.soultech.internal.entity.items.IHasUnlockConditions;
import com.talexs.soultech.internal.entity.items.INeedPrepositions;
import com.talexs.soultech.internal.entity.items.ItemClass;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.preposition.IPreposition;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@ItemClass(FeatherClass.class)
public abstract class BaseFeatherCompressedItem extends SoulTechItem implements IHasUnlockConditions, INeedPrepositions {
    public BaseFeatherCompressedItem(@NotNull String ID, @NotNull ItemStack stack) {
        super(ID, stack);
    }

    @Override
    public Set<IPreposition> getPreposition() {
        return null;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(15));
    }

}
