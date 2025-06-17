package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.item;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.item.MachineItem;
import org.bukkit.inventory.ItemStack;

public abstract class MachineBlockItem extends MachineItem {

    public MachineBlockItem(String ID, ItemStack stack) {

        super(ID, stack);

    }

    public abstract String onSave();

    public abstract void onLoad(String str);

}
