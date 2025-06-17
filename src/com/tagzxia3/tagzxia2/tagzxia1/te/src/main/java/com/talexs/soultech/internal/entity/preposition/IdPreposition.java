package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.internal.entity.preposition;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.preposition.IPreposition;
import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.classfies.ClassifiesAddon;
import com.talexs.soultech.internal.PlayerSoul;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IdPreposition implements IPreposition {

    private final String id;

    public Classifies getPreposition() {
        ClassifiesAddon classifiesAddon = AddonHolder.getINSTANCE().get(ClassifiesAddon.class);

        return classifiesAddon.getClassifies(id);
    }

    @Override
    public boolean allow(PlayerSoul ps) {
        Classifies preposition = getPreposition();

        return ps.doCategoryUnlock(preposition.getID());
    }

    @Override
    public String display() {
        return getPreposition().getName();
    }
}
