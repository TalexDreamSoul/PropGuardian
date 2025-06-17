package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.tools.BaseToolItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class GoldHammerEmbryo extends BaseToolItem {


    public GoldHammerEmbryo() {
        super("gold_hammer_embryo", new ItemBuilder(Material.GOLD_BLOCK)
                .addEnchant(Enchantment.DIG_SPEED, 1).addFlag(ItemFlag.HIDE_ENCHANTS).setName("§f◆ 破碎锤胚胎(金)").setLore("", "§8| §e破坏总比创造易!", "&8| &a这只是胚胎，需要冶炼孵化.").toItemStack());

    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired("fire_ingot_block")
                .addRequired("st_break_hammer_iron")
                .addRequired("fire_ingot_block")
                .addRequired("st_rough_mesh")
                .addRequired("st_strengthen_gold_stick")
                .addRequired("st_rough_mesh")
                .addRequired("fire_ingot_block")
                .addRequired("st_strengthen_gold_stick")
                .addRequired("fire_ingot_block");
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(50));
    }
}
