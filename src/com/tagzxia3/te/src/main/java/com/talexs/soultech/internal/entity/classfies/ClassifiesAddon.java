package com.tagzxia3.te.src.main.java.com.talexs.soultech.internal.entity.classfies;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.addon.BaseAddon;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassifiesAddon extends BaseAddon {

    @Getter
    private final com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies root = new com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies("root", "灵魂科技");

    private final Map<String, com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies> classifiesMap = new HashMap<>(32);

    private final Map<Class<? extends BaseClassifiesCreator>, com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies> classifiesClzMap = new HashMap<>(32);

    public Set<com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies> getClassifies() {
        return new HashSet<>(classifiesMap.values());
    }

    public Set<com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies> deepClassifies(com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies classifies) {
        Set<com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies> classifiesSet = new HashSet<>();

        classifiesSet.add(classifies);

        classifies.getChildren().forEach(classifies1 -> classifiesSet.addAll(deepClassifies(classifies1)));

        return classifiesSet;
    }

    public Set<com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies> deepRootClassifies() {
        return deepClassifies(root);
    }

    public com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies getClassifies(String id) {
        return classifiesMap.get(id);
    }

    public com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.Classifies getClassifies(Class<? extends BaseClassifiesCreator> clazz) {
        return classifiesClzMap.get(clazz);
    }

    public void addClassifies(Class<? extends BaseClassifiesCreator> clz, Classifies classifies) {

        if ( classifies.getID().equals("root") )
            throw new RuntimeException("Illegal action: can't add root classifies.");

        classifiesMap.put(classifies.getID(), classifies);
        classifiesClzMap.put(clz, classifies);
    }

}
