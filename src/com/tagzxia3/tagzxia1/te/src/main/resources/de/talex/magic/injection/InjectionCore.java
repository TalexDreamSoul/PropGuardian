package com.tagzxia3.tagzxia1.te.src.main.resources.de.talex.magic.injection;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import pubsher.talexsoultech.talex.guider.category.RecipeObject;
import pubsher.talexsoultech.talex.machine.advanced_workbench.WorkBenchRecipe;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.SoulTechItem;

/**
 * <br /> {@link pubsher.talexsoultech.talex.magic.injection Package }
 *
 * @author TalexDreamSoul
 * @date 2021/8/24 5:46 <br /> Project: TalexSoulTech <br />
 */
public class InjectionCore extends SoulTechItem {

    public InjectionCore() {

        super("magic_injection_core", new ItemBuilder(Material.CHORUS_PLANT)

                .setName("§5注魔核心")

                .setLore("", "§8> §5万物, 万灵, 万魔..", "")

                .addEnchant(Enchantment.DURABILITY, 1).addFlag(ItemFlag.HIDE_ENCHANTS)

                .toItemStack());

    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe("magic_injection_core", this)

                .addRequired(Material.OBSIDIAN)
                .addRequired("fire_ingot_block")
                .addRequired(Material.OBSIDIAN)
                .addRequired("fire_ingot_block")
                .addRequired(Material.EMERALD_BLOCK)
                .addRequired("fire_ingot_block")
                .addRequired(Material.OBSIDIAN)
                .addRequired("fire_ingot_block")
                .addRequired(Material.OBSIDIAN)

                ;

    }

}
