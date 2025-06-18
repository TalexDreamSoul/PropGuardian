package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.material.strengthen_stick_compressed;

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

public class HardenStick extends BaseMaterialItem {
    public HardenStick() {
        super("st_harden_stick_compressed", new ItemBuilder(Material.STICK)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1)
                .setName("&f压缩硬化木棍").toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .setAmount(8)
                .addRequiredNull()
                .addRequired("compress_wood_2")
                .addRequiredNull()
                .addRequired("compress_wood_2")
                .addRequired(new MineCraftItem(Material.COAL_BLOCK))
                .addRequired("compress_wood_2")
                .addRequiredNull()
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(10));
    }

}
