package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.classifies;

import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ElectricityClass extends BaseClassifiesCreator {
    @Override
    public String getId() {
        return "st_electricity";
    }

    @Override
    public String getName() {
        return "电力学";
    }

    @Override
    public ItemStack getDisplayStack() {
        return new ItemBuilder(Material.REDSTONE).setLore("", "&8| &f皖气动力之源", "&8| &7即可，开启工业革命", "&8| &7浓缩的机械，精密的制造..", "")
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#C3D7F1:#FF99A4>电力学")).toItemStack();
    }

    public ElectricityClass() {

        addUnlockCondition(new RankCondition(12));

    }

}
