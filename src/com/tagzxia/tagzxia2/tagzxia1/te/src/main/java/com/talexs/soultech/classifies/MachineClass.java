package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.classifies;

import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MachineClass extends BaseClassifiesCreator {
    @Override
    public String getId() {
        return "st_machine";
    }

    @Override
    public String getName() {
        return "机械学";
    }

    @Override
    public ItemStack getDisplayStack() {
        return new ItemBuilder(Material.FURNACE).setLore("", "&8| &f机械距今至少有万年历史", "&8| &7古人非常擅长制造小型机械", "&8| &7小心点，别卷进去了!", "")
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#2B2D30:#24201B>机械学")).toItemStack();
    }

    public MachineClass() {

        addUnlockCondition(new RankCondition(10));

    }

}
