package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.classifies;

import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AlchemyClass extends BaseClassifiesCreator {
    @Override
    public String getId() {
        return "st_alchemy";
    }

    @Override
    public String getName() {
        return "炼金学";
    }

    @Override
    public ItemStack getDisplayStack() {
        return new ItemBuilder(Material.BREWING_STAND).setLore("", "&8| &f炼金术最初是炼制不老丹", "&8| &7后来被用于一些奇怪的目的", "&8| &7斑驳的东西居然不会爆炸..", "")
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#9F78BA:#C61FCD>炼金学")).toItemStack();
    }

    public AlchemyClass() {

        addUnlockCondition(new RankCondition(3));

    }

}
