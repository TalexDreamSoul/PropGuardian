package com.tagzxia3.te.src.main.java.com.talexs.soultech.internal.entity.items;

import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;

import java.util.Set;

public interface IHasUnlockConditions {

    public Set<IUnlockCondition> getUnlockConditions();

}
