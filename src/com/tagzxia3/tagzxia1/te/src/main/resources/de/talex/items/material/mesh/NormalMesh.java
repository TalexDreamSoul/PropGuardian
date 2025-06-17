package com.tagzxia3.tagzxia1.te.src.main.resources.de.talex.items.material.mesh;

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
public class NormalMesh extends BaseGriddleMesh {

    public NormalMesh() {

        super(new ItemBuilder(Material.TRAP_DOOR).setName("§f普通筛网").setLore("", "§8> §a普通的筛网.", "", "§f耐久: §c20", ""), (short) 20);
    }

    @Override
    public boolean isIron() {

        return false;
    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe("mesh_normal", this)

                .addRequired(new MineCraftItem(Material.LOG))
                .addRequired(new MineCraftItem(Material.STRING))
                .addRequired(new MineCraftItem(Material.LOG))
                .addRequired(new MineCraftItem(Material.STRING))
                .addRequired("fire_ingot")
                .addRequired(new MineCraftItem(Material.STRING))
                .addRequired(new MineCraftItem(Material.LOG))
                .addRequired(new MineCraftItem(Material.STRING))
                .addRequired(new MineCraftItem(Material.LOG))

                ;
    }

}
