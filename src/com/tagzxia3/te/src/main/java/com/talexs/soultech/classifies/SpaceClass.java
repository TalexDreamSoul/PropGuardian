package com.tagzxia3.te.src.main.java.com.talexs.soultech.classifies;

import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpaceClass extends BaseClassifiesCreator {
    @Override
    public String getId() {
        return "st_space";
    }

    @Override
    public String getName() {
        return "空间学";
    }

    @Override
    public ItemStack getDisplayStack() {
        return new ItemBuilder(Material.GLOWSTONE_DUST).setLore("", "&8| &f最后的归宿，空间", "&8| &5诡异 &b波动 &8的空间脉络", "&8| &7你可能需要特殊物品来发现..", "")
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#2B2D30:#24201B>空间学")).toItemStack();
    }

    public SpaceClass() {

        addUnlockCondition(new RankCondition(6));
        addUnlockCondition(new LevelCondition(100));

    }

}
