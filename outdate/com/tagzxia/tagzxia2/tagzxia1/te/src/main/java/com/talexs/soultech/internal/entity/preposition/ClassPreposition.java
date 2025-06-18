package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.internal.entity.preposition;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.preposition.IPreposition;
import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.classfies.ClassifiesAddon;
import com.talexs.soultech.internal.PlayerSoul;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class ClassPreposition implements IPreposition {

    private final Class<?> clazz;

    public Classifies getPreposition() {
        ClassifiesAddon classifiesAddon = AddonHolder.getINSTANCE().get(ClassifiesAddon.class);

        Optional<Classifies> first = classifiesAddon.deepRootClassifies().stream().filter(classifies -> classifies.getClass().isAssignableFrom(clazz)).findFirst();

        if (!first.isPresent()) {
            throw new RuntimeException("Classifies not found!");
        }

        return first.get();
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
