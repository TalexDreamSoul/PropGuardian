package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.item;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class MachineItem extends SoulTechItem {


    public MachineItem(String ID, ItemStack stack) {

        super(ID, stack);
    }

    public abstract void onClickedMachineItemBlock(PlayerData playerData, PlayerInteractEvent event);

}
