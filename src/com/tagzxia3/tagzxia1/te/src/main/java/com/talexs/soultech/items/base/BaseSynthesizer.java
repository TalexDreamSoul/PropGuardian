package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.base;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.base.BaseItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.Set;

public class BaseSynthesizer extends BaseItem {
    public BaseSynthesizer() {
        super("st_machine_Synthesizer", new ItemBuilder(Material.DROPPER)
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#1E1F22:#2B2D30>自动合成器"))
                .setLore("", "&8| &e解放双手时刻", "&8| &7插拔插槽升级", "&8| &7试试自动合成", "",
                                "&8| &7中间由上至下分别构成：",
                                "&8| &f箱子、投掷器、红石块",
                                "&8| &7两侧构造均一致，为：",
                                "&8| &f铁活板门、活塞，哭泣的黑曜石",
                                "&8| &e蹲下右键 &7以打开控制菜单",
                                "")
                .toItemStack());
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(125));
    }
}
