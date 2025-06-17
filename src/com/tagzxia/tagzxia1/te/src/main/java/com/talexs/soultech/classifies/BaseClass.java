package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.classifies;

import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BaseClass extends BaseClassifiesCreator {
    @Override
    public String getId() {
        return "st_base";
    }

    @Override
    public String getName() {
        return "基础学";
    }

    @Override
    public ItemStack getDisplayStack() {
        return new ItemBuilder(Material.DISPENSER).setLore("", "&8| &f世界创造了我们 我们创造了世界", "&8| &7在这里, 学习各种基本机器", "&8| &7解锁机器后才可使用..", "")
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#1E1F2e:#eBeDe0>基础学")).toItemStack();
    }

    public BaseClass() {

        addUnlockCondition(new RankCondition(3));

    }

}
