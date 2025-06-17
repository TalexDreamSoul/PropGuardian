package com.tagzxia3.tagzxia2.te.src.main.resources.de.talex.items.material.others;

import org.bukkit.Material;
import pubsher.talexsoultech.talex.guider.category.RecipeObject;
import pubsher.talexsoultech.talex.machine.griddle.GriddleRecipe;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.MineCraftItem;
import pubsher.talexsoultech.utils.item.SoulTechItem;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.items.material.others }
 *
 * @author TalexDreamSoul
 * @date 2021/8/16 17:26
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class SuperString extends SoulTechItem {

    public SuperString() {

        super("super_string", new ItemBuilder(Material.STRING).setName("§f强力丝线").toItemStack());
    }

    @Override
    public RecipeObject getRecipe() {

        return new GriddleRecipe("super_string", this).setRandom(0.02f).setAmount(2).setNeed(new MineCraftItem(Material.WOOL));
    }

}
