package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.space.tank;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.space.tank.BaseTank;
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
public class IronTank extends BaseTank {

    public IronTank() {

        super("iron_tank", 5000);

    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe("iron_tank", this)

                .addRequiredNull()
                .addRequiredNull()
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.IRON_BLOCK))
                .addRequired("tank_normal_tank")
                .addRequired(new MineCraftItem(Material.IRON_BLOCK))
                .addRequired("space_fire_dust")
                .addRequired(new MineCraftItem(Material.IRON_BLOCK))
                .addRequired("space_fire_dust")
                ;
    }

}
