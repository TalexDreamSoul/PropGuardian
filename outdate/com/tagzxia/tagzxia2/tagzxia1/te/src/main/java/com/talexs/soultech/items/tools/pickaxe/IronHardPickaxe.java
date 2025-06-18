package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.tools.pickaxe;

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

public class IronHardPickaxe extends BaseToolItem {
    public IronHardPickaxe() {
        super("st_iron_hard_pickaxe", new ItemBuilder(Material.IRON_PICKAXE).setName("&f硬化铁镐")
                .addEnchant(Enchantment.DIG_SPEED, 5)
                .addEnchant(Enchantment.SILK_TOUCH, 1)
                .setUnbreakable().toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_iron_hard_pickaxe", this)
                .addRequired("st_stone_hard_pickaxe")
                .addRequired("st_harden_core")
                .addRequired("st_stone_hard_pickaxe")
                .addRequiredNull()
                .addRequired("st_strengthen_iron_stick")
                .addRequiredNull()
                .addRequiredNull()
                .addRequired("st_strengthen_iron_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(15));
    }

}
