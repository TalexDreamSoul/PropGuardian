package com.tagzxia.te.src.main.java.com.talexs.soultech.items.tools.pickaxe;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.items.tools.BaseToolItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Set;

public class StoneHardPickaxe extends BaseToolItem {
    public StoneHardPickaxe() {
        super("st_stone_hard_pickaxe", new ItemBuilder(Material.STONE_PICKAXE).setName("&f硬化石镐")
                .addEnchant(Enchantment.DIG_SPEED, 3)
                .setUnbreakable().toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_stone_hard_pickaxe", this)
                .addRequired("st_hard_pickaxe")
                .addRequired("st_harden_core")
                .addRequired("st_hard_pickaxe")
                .addRequiredNull()
                .addRequired("st_strengthen_copper_stick")
                .addRequiredNull()
                .addRequiredNull()
                .addRequired("st_strengthen_copper_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(10));
    }

}
