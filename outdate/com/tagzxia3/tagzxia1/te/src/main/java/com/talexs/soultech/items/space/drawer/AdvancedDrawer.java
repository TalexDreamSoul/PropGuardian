package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.space.drawer;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.space.drawer.BaseDrawer;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.internal.RecipeObject;

public class AdvancedDrawer extends BaseDrawer {
    public AdvancedDrawer() {
        super("advanced", 12800);

        range = 3;
    }

    @Override
    public RecipeObject getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("drawer_high")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                ;
    }
}
