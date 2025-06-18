package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.items.tools.pickaxe;

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

public class GoldHardPickaxe extends BaseToolItem {
    public GoldHardPickaxe() {
        super("st_gold_hard_pickaxe", new ItemBuilder(Material.GOLDEN_PICKAXE).setName("&f硬化金镐")
                .addEnchant(Enchantment.DIG_SPEED, 5)
                .addEnchant(Enchantment.SILK_TOUCH, 1)
                .setUnbreakable().toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_gold_hard_pickaxe", this)
                .addRequired("st_iron_hard_pickaxe")
                .addRequired("st_harden_core")
                .addRequired("st_iron_hard_pickaxe")
                .addRequiredNull()
                .addRequired("st_strengthen_gold_stick")
                .addRequiredNull()
                .addRequiredNull()
                .addRequired("st_strengthen_gold_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(20));
    }

}
