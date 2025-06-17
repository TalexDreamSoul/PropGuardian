package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.base;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.base.BaseItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.alchemy.BaseAlchemyItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.Set;

public class BaseFurnaceCauldron extends BaseItem {
    public BaseFurnaceCauldron() {
        super("st_machine_FurnaceCauldron", new ItemBuilder(Material.CAULDRON)
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#1E1F22:#2B2D30>冶炼熔炉"))
                .setLore("", "§8| §7高达 1000℃ 的温度", "&8| &7能够炼化大部分物品", "&8| &e在坩埚下放置岩浆来启用", "")
                .toItemStack());
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(5));
    }
}
