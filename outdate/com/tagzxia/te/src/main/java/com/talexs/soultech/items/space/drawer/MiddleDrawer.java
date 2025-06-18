package com.tagzxia.te.src.main.java.com.talexs.soultech.items.space.drawer;

import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.internal.RecipeObject;

public class MiddleDrawer extends BaseDrawer {
    public MiddleDrawer() {
        super("middle", 5120);

        range = 2;
    }

    @Override
    public RecipeObject getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("drawer_strengthen")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                ;
    }
}
