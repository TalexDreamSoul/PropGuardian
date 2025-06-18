package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.material.strengthen_stick_compressed;

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

public class SencIronStick extends BaseMaterialItem {
    public SencIronStick() {
        super("st_strengthen_compressed_iron_stick", new ItemBuilder(Material.STICK)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1)
                .setName("&f压缩强化木棍 (铁)").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequiredNull()
                .addRequired("st_strengthen_compressed_copper_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_compressed_copper_stick")
                .addRequired(new MineCraftItem(Material.IRON_BLOCK))
                .addRequired("st_strengthen_compressed_copper_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_compressed_copper_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(20));
    }

}
