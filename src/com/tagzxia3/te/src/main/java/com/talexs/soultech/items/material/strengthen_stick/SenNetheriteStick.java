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

public class SenNetheriteStick extends BaseMaterialItem {
    public SenNetheriteStick() {
        super("st_strengthen_netherite_stick", new ItemBuilder(Material.STICK)
                .addEnchant(Enchantment.ARROW_INFINITE, 1).addFlag(ItemFlag.HIDE_ENCHANTS)
                .setName("&e强化木棍 (下界合金)").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_strengthen_netherite_stick", this)
                .addRequiredNull()
                .addRequired("st_strengthen_diamond_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_diamond_stick")
                .addRequired(new MineCraftItem(Material.NETHERITE_BLOCK))
                .addRequired("st_strengthen_diamond_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_diamond_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(13));
    }

}
