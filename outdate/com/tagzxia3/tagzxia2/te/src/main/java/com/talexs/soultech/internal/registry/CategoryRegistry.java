package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.internal.registry;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.registry.IRegistry;
import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.classfies.ClassifiesAddon;
import com.talexs.talexessential.utils.LogUtil;

import java.util.*;

public class CategoryRegistry implements IRegistry {

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Set<String> getScanFolders() {
        return Set.of("com.talexs.soultech.classifies");
    }

    @Override
    public boolean isAssignable(Class<?> clazz) {
        return BaseClassifiesCreator.class.isAssignableFrom(clazz);
    }

    static Map<Classifies, BaseClassifiesCreator> map = new HashMap<>();

    @Override
    public boolean register(Class<?> clazz) throws InstantiationException, IllegalAccessException {

        BaseClassifiesCreator bcc = (BaseClassifiesCreator) clazz.newInstance();

        Classifies classifies = new Classifies(
                bcc.getId(),
                bcc.getName(),
                bcc.getDisplayStack()
        );

        classifies.setPriority(bcc.getPriority());
        classifies.getPreposition().addAll(bcc.getPreposition());
        classifies.getUnlockConditions().addAll(bcc.getUnlockConditions());

        map.put(classifies, bcc);

        ClassifiesAddon classifiesAddon = AddonHolder.getINSTANCE().get(ClassifiesAddon.class);

        classifiesAddon.addClassifies(bcc.getClass(), classifies);
        classifiesAddon.getRoot().addChild(classifies);

        return true;
    }

    @Override
    public void onceScanDone(int amo) {

        ClassifiesAddon classifiesAddon = AddonHolder.getINSTANCE().get(ClassifiesAddon.class);

        map.forEach((key, value) -> {

            if ( value.getFatherClassify() != null ) {
                Classifies classifies = classifiesAddon.getClassifies(value.getFatherClassify());

                if (classifies == null) {
                    throw new RuntimeException("Father classifies not found: " + value.getFatherClassify().getName());
                }

                key.setFatherCategory(classifies);
            }

        });

        LogUtil.log("&7[CategoryRegistry] Load &e" + map.size() + " &7classifies done.");

    }
}
