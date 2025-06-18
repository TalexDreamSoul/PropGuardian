package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.base;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.base.BaseItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.alchemy.BaseAlchemyItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.Set;

public class BaseAdvancedWorkbench extends BaseItem {
    public BaseAdvancedWorkbench() {
        super("st_machine_AdvancedWorkBench", new ItemBuilder(Material.CRAFTING_TABLE)
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#1E1F22:#2B2D30>高级工作台"))
                .setLore("", "§8| §f高级工作台", "&8| &7至关重要的一步", "&8| &e在工作台上放置一个玻璃来建造", "")
                .toItemStack());
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(5));
    }
}
