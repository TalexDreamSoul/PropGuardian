package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.classifies;

import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ToolClass extends BaseClassifiesCreator {
    @Override
    public String getId() {
        return "st_tools";
    }

    @Override
    public String getName() {
        return "工具学";
    }

    @Override
    public ItemStack getDisplayStack() {
        return new ItemBuilder(Material.STONE_PICKAXE).setLore("", "&8| &f工具，触达万物之边", "&8| &7衍生的万物琳琅", "&8| &7别伤到自己了，小子", "")
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#6580BA:#2A3959>工具学")).toItemStack();
    }

    public ToolClass() {

        addUnlockCondition(new RankCondition(7));

    }

}
