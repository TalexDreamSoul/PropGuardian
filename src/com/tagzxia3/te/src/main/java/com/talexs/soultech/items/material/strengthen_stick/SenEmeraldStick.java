package com.tagzxia3.te.src.main.java.com.talexs.soultech.items.material.strengthen_stick;

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

public class SenEmeraldStick extends BaseMaterialItem {
    public SenEmeraldStick() {
        super("st_strengthen_emerald_stick", new ItemBuilder(Material.STICK)
                .addEnchant(Enchantment.ARROW_INFINITE, 1).addFlag(ItemFlag.HIDE_ENCHANTS)
                .setName("&f强化木棍 (绿宝石)").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_strengthen_emerald_stick", this)
                .addRequiredNull()
                .addRequired("st_strengthen_diamond_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_diamond_stick")
                .addRequired(new MineCraftItem(Material.EMERALD_BLOCK))
                .addRequired("st_strengthen_diamond_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_diamond_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(11));
    }

}
