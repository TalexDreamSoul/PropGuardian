package com.tagzxia2.te.src.main.java.com.talexs.soultech.internal.entity.classfies;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.IClassifiesCreator;
import com.talexs.soultech.internal.entity.preposition.IPreposition;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseClassifiesCreator implements IClassifiesCreator {

    private Set<IPreposition> preposition = new HashSet<>();

    private Set<IUnlockCondition> unlockConditions = new HashSet<>();

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public Class<? extends BaseClassifiesCreator> getFatherClassify() {
        return null;
    }

    @Override
    public Set<IPreposition> getPreposition() {
        return preposition;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return unlockConditions;
    }

    public BaseClassifiesCreator addUnlockCondition(IUnlockCondition condition) {

        unlockConditions.add(condition);

        return this;
    }

    public BaseClassifiesCreator addPreposition(IPreposition preposition) {

        this.preposition.add(preposition);

        return this;
    }

    public BaseClassifiesCreator delPreposition(IPreposition preposition) {

        this.preposition.remove(preposition);

        return this;
    }

}
