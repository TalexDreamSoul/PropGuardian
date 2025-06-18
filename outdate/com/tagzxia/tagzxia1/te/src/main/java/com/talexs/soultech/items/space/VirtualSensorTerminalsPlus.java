package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.items.space;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.space.BaseSpaceItem;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

public class VirtualSensorTerminalsPlus extends BaseSpaceItem {

    public VirtualSensorTerminalsPlus() {
        super("virtuals_sensor_terminals_plus", new ItemBuilder(Material.CALIBRATED_SCULK_SENSOR)
                .setName("&b强化虚尽感测终端")
                .setLore(
                        "",
                        "&8| &7虚无的奥空术士，采用无止尽的手段",
                        "&8| &7欲想感受虚之尽，这似乎是唯一方式",
                        "&8| &7虚空中点点波动，映辉着真实的脉络",
                        "",
                        "&8| &7感测能力: &eII",
                        "&8| &7感测范围: &e5x5x5",
                        "&8| &7感测状态: &7未激活",
                        ""
                )
                .toItemStack());
    }

    public RecipeObject getRecipe() {
        return new WorkBenchRecipe(this.getID(), this)
                .addRequired(new MineCraftItem(Material.NETHERITE_SCRAP))
                .addRequired(new MineCraftItem(Material.AMETHYST_SHARD))
                .addRequired(new MineCraftItem(Material.NETHERITE_SCRAP))
                .addRequired(new MineCraftItem(Material.AMETHYST_SHARD))
                .addRequired("virtuals_sensor_terminals")
                .addRequired(new MineCraftItem(Material.AMETHYST_SHARD))
                .addRequired(new MineCraftItem(Material.NETHERITE_SCRAP))
                .addRequired(new MineCraftItem(Material.AMETHYST_SHARD))
                .addRequired(new MineCraftItem(Material.NETHERITE_SCRAP))
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(45));
    }

}
