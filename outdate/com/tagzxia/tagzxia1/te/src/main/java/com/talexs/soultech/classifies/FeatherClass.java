package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.classifies;

import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FeatherClass extends BaseClassifiesCreator {
    @Override
    public String getId() {
        return "st_feather";
    }

    @Override
    public String getName() {
        return "轻辉学";
    }

    @Override
    public ItemStack getDisplayStack() {
        return new ItemBuilder(Material.FEATHER).setLore("", "&8| &f轻辉学的来源非常特殊", "&8| &7像是鸡的羽毛般会让你蹦起来", "&8| &7羽毛的起源有待深究..", "")
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#A5B4BA:#454547>轻辉学")).toItemStack();
    }

    public FeatherClass() {

        addUnlockCondition(new RankCondition(8));

    }

}
