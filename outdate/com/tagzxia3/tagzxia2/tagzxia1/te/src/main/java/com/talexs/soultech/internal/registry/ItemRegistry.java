package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.internal.registry;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.registry.IRegistry;
import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.classfies.ClassifiesAddon;
import com.talexs.soultech.internal.entity.items.*;
import com.talexs.soultech.internal.entity.tech_object.TechObject;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.utils.LogUtil;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemRegistry implements IRegistry {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public Set<String> getScanFolders() {
        return Set.of("com.talexs.soultech.items");
    }

    @Override
    public boolean isAssignable(Class<?> clazz) {
        return SoulTechItem.class.isAssignableFrom(clazz);
    }

    static List<SoulTechItem> itemList = new ArrayList<>();

    @Override
    public boolean register(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        SoulTechItem bcc = (SoulTechItem) clazz.newInstance();

        // Reflect get annotation
        ItemClass annotation = bcc.getClass().getAnnotation(ItemClass.class);

        if ( annotation == null ) {
            LogUtil.log("&e&l[ItemRegistry] The class " + bcc.getClass().getName() + " does not have an @ItemClass annotation.");

            return false;
        }

        itemList.add(bcc);

        return true;
    }

    @Override
    public void onceScanDone(int amo) {

        itemList.forEach(bcc -> {

            ItemClass annotation = bcc.getClass().getAnnotation(ItemClass.class);

            TechObject to = new TechObject(bcc.getID(), bcc.getID(), bcc.getRecipe()) {
                @Override
                public ItemStack getItemStack() {
                    return bcc.build();
                }

            };

            if ( bcc instanceof INeedPrepositions  inp ) {
                if ( inp.getPreposition() != null )
                    to.getPreposition().addAll(inp.getPreposition());
            }

            if ( bcc instanceof IHasUnlockConditions inp ) {
                if ( inp.getUnlockConditions() != null )
                    to.getUnlockConditions().addAll(inp.getUnlockConditions());
            }

            Classifies classifies = AddonHolder.getINSTANCE().get(ClassifiesAddon.class).getClassifies(annotation.value());

            if ( classifies == null ) {
                LogUtil.log("&e&l[ItemRegistry] The class &c&l" + bcc.getClass().getName() + " &e&ldoes not have father classifies(NULL).");

                return;
            }

            BaseItemCreator baseItemCreator = new BaseItemCreator(bcc);

            to.setFatherCategory(classifies);
            classifies.addChild(to);
            bcc.setOwnCategoryObject(to);
            AddonHolder.getINSTANCE().get(ClassifiesAddon.class).addClassifies(baseItemCreator.getClass(), to);

        });

        LogUtil.log("&7[ItemRegistry] Load &e" + (itemList.size()) + "/" + amo + " &7items done.");

        itemList.clear();
    }
}
