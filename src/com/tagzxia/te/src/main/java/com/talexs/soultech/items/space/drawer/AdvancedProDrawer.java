package com.tagzxia.te.src.main.java.com.talexs.soultech.items.space.drawer;

import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.internal.RecipeObject;

public class AdvancedProDrawer extends BaseDrawer {
    public AdvancedProDrawer() {
        super("advanced_pro", 51200);

        range = 5;
    }

    @Override
    public RecipeObject getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("drawer_advanced_plus")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                ;
    }
}
