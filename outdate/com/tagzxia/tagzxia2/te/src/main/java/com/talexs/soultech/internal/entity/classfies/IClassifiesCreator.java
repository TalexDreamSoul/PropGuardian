package com.tagzxia2.te.src.main.java.com.talexs.soultech.internal.entity.classfies;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.preposition.IPreposition;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public interface IClassifiesCreator {

    public int getPriority();

    public String getId();

    public String getName();

    public ItemStack getDisplayStack();

    public Class<? extends BaseClassifiesCreator> getFatherClassify();

    public Set<IPreposition> getPreposition();

    public Set<IUnlockCondition> getUnlockConditions();

}
