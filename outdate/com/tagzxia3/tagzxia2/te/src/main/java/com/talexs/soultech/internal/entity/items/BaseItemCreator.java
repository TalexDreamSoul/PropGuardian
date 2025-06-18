package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.internal.entity.items;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import org.bukkit.inventory.ItemStack;

public class BaseItemCreator extends BaseClassifiesCreator {

    private SoulTechItem item;

    public BaseItemCreator(SoulTechItem item) {
        this.item = item;
    }

    @Override
    public String getId() {
        return item.getID();
    }

    @Override
    public String getName() {
        return item.getID();
    }

    @Override
    public ItemStack getDisplayStack() {
        return item.build();
    }
}
