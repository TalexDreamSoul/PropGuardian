package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.machine;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.machine.BaseMachineItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class EmptyMark extends BaseMachineItem {
    public EmptyMark() {
        super("st_empty_mark", new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName("&f空白标记物")
                .setLore(
                        "",
                        "&8| &7放置到 &e自动合成器 &7中以忽略插槽位置物品",
                        "&8| &7加速，大步迈进自动化新时代！",
                        ""
                )
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequiredNull()
                .addRequired("st_strengthen_iron_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_iron_stick")
                .addRequired(new MineCraftItem(Material.EMERALD))
                .addRequired("st_strengthen_iron_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_iron_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(20));
    }

}
