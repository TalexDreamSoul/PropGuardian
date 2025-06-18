package com.tagzxia.te.src.main.java.com.talexs.soultech.items.base;

import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.alchemy.BaseAlchemyItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.Set;

public class BaseNormalCompressor extends BaseItem {
    public BaseNormalCompressor() {
        super("st_machine_NormalCompressor", new ItemBuilder(Material.CRAFTING_TABLE)
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#1E1F22:#2B2D30>高级工作台"))
                .setLore("", "&8| &e压缩压力压强", "&8| &7无尽必不可少", "&8| &7层层挤压能力", "&8| &7现在就大不同", "",
                                "&8| &7中间由上至下分别构成：",
                                "&8| &f磁石、玻璃、炼药锅（岩浆）",
                                "&8| &7两侧构造均一致，为：",
                                "&8| &f深板岩瓦台阶、切石机，粘性活塞", "")
                .toItemStack());
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(15));
    }
}
