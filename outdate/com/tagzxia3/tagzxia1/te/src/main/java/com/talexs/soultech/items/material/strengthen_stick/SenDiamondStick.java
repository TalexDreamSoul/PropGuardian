package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.material.strengthen_stick;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.Set;

public class SenDiamondStick extends BaseMaterialItem {
    public SenDiamondStick() {
        super("st_strengthen_diamond_stick", new ItemBuilder(Material.STICK)
                .addEnchant(Enchantment.ARROW_INFINITE, 1).addFlag(ItemFlag.HIDE_ENCHANTS)
                .setName("&f强化木棍 (钻)").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_strengthen_diamond_stick", this)
                .addRequiredNull()
                .addRequired("st_strengthen_gold_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_gold_stick")
                .addRequired(new MineCraftItem(Material.DIAMOND_BLOCK))
                .addRequired("st_strengthen_gold_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_gold_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(9));
    }

}
