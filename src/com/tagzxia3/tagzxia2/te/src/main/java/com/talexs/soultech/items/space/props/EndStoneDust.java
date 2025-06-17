package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.items.space.props;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.items.space.BaseSpaceItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

import java.util.Set;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.items.space }
 *
 * @author TalexDreamSoul
 * @date 2021/8/17 3:35
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class EndStoneDust extends BaseSpaceItem {

    public EndStoneDust() {

        super("end_stone_dust", new ItemBuilder(Material.BEETROOT_SEEDS)
                .setName("§e末影粉尘").setLore("", "§8| §e虚幻的能量, 微不足道..", "").toItemStack());

    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe(getID(), this).setAmount(8)
                .addRequired(new MineCraftItem(Material.BLAZE_POWDER))
                .addRequired(new MineCraftItem(Material.ENDER_PEARL))
                .addRequired(new MineCraftItem(Material.BLAZE_POWDER))
                .addRequired(new MineCraftItem(Material.ENDER_PEARL))
                .addRequired(new MineCraftItem(Material.OBSIDIAN))
                .addRequired(new MineCraftItem(Material.ENDER_PEARL))
                .addRequired(new MineCraftItem(Material.BLAZE_POWDER))
                .addRequired(new MineCraftItem(Material.ENDER_PEARL))
                .addRequired(new MineCraftItem(Material.BLAZE_POWDER))
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(30));
    }

}
