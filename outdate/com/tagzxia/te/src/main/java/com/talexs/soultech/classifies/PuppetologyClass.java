package com.tagzxia.te.src.main.java.com.talexs.soultech.classifies;

import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PuppetologyClass extends BaseClassifiesCreator {
    @Override
    public String getId() {
        return "st_puppetology";
    }

    @Override
    public String getName() {
        return "傀儡学";
    }

    @Override
    public ItemStack getDisplayStack() {
        return new ItemBuilder(Material.IRON_GOLEM_SPAWN_EGG).setLore("", "&8| &f傀儡学术起初是很邪恶的", "&8| &7随着时代进步，不再会出现练巫傀儡", "&8| &7傀儡学科真的很好用..", "")
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#414141:#41372D>傀儡学")).toItemStack();
    }

    public PuppetologyClass() {

        addUnlockCondition(new RankCondition(5));

    }

}
