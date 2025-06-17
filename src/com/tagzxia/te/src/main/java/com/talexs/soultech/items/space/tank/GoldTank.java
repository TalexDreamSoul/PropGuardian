package com.tagzxia.te.src.main.java.com.talexs.soultech.items.space.tank;

import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.items.tank }
 *
 * @author TalexDreamSoul
 * @date 2021/8/17 0:32
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class GoldTank extends BaseTank {

    public GoldTank() {

        super("gold_tank", 10000);

    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe("gold_tank", this)

                .addRequiredNull()
                .addRequiredNull()
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.GOLD_BLOCK))
                .addRequired("tank_iron_tank")
                .addRequired(new MineCraftItem(Material.GOLD_BLOCK))
                .addRequired("space_fire_dust")
                .addRequired(new MineCraftItem(Material.GOLD_BLOCK))
                .addRequired("space_fire_dust")
                ;
    }

}
