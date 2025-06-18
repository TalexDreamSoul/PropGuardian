package com.tagzxia3.tagzxia2.te.src.main.resources.de.talex.items.material.mesh;

import org.bukkit.Material;
import pubsher.talexsoultech.talex.guider.category.RecipeObject;
import pubsher.talexsoultech.talex.machine.advanced_workbench.WorkBenchRecipe;
import pubsher.talexsoultech.talex.machine.griddle.BaseGriddleMesh;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.MineCraftItem;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.items.material.mesh }
 *
 * @author TalexDreamSoul
 * @date 2021/8/16 11:35
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class NormalMeshPlus extends BaseGriddleMesh {

    public NormalMeshPlus() {

        super(new ItemBuilder(Material.TRAP_DOOR).setName("§f高级筛网").setLore("", "§8> §a高级的筛网.", "", "§f耐久: §c120", ""), (short) 120);
    }

    @Override
    public boolean isIron() {

        return false;
    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe("mesh_normal_plus", this)

                .addRequired(new MineCraftItem(Material.LOG))
                .addRequired("super_string")
                .addRequired(new MineCraftItem(Material.LOG))
                .addRequired("super_string")
                .addRequired("fire_ingot_block")
                .addRequired("super_string")
                .addRequired(new MineCraftItem(Material.LOG))
                .addRequired("super_string")
                .addRequired(new MineCraftItem(Material.LOG))

                ;
    }

}
