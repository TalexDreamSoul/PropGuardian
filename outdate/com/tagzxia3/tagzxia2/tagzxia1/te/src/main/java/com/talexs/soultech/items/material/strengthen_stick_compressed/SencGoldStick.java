package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.material.strengthen_stick_compressed;

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

public class SencGoldStick extends BaseMaterialItem {
    public SencGoldStick() {
        super("st_strengthen_compressed_gold_stick", new ItemBuilder(Material.STICK)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1)
                .setName("&f压缩强化木棍 (金)").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequiredNull()
                .addRequired("st_strengthen_compressed_iron_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_compressed_iron_stick")
                .addRequired(new MineCraftItem(Material.GOLD_BLOCK))
                .addRequired("st_strengthen_compressed_iron_stick")
                .addRequiredNull()
                .addRequired("st_strengthen_compressed_iron_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(30));
    }

}
