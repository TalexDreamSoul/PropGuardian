package com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies;

import com.talexs.soultech.addon.BaseAddon;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassifiesAddon extends BaseAddon {

    @Getter
    private final Classifies root = new Classifies("root", "灵魂科技");

    private final Map<String, Classifies> classifiesMap = new HashMap<>(32);

    private final Map<Class<? extends BaseClassifiesCreator>, Classifies> classifiesClzMap = new HashMap<>(32);

    public Set<Classifies> getClassifies() {
        return new HashSet<>(classifiesMap.values());
    }

    public Set<Classifies> deepClassifies(Classifies classifies) {
        Set<Classifies> classifiesSet = new HashSet<>();

        classifiesSet.add(classifies);

        classifies.getChildren().forEach(classifies1 -> classifiesSet.addAll(deepClassifies(classifies1)));

        return classifiesSet;
    }

    public Set<Classifies> deepRootClassifies() {
        return deepClassifies(root);
    }

    public Classifies getClassifies(String id) {
        return classifiesMap.get(id);
    }

    public Classifies getClassifies(Class<? extends BaseClassifiesCreator> clazz) {
        return classifiesClzMap.get(clazz);
    }

    public void addClassifies(Class<? extends BaseClassifiesCreator> clz, Classifies classifies) {

        if ( classifies.getID().equals("root") )
            throw new RuntimeException("Illegal action: can't add root classifies.");

        classifiesMap.put(classifies.getID(), classifies);
        classifiesClzMap.put(clz, classifies);
    }

}
