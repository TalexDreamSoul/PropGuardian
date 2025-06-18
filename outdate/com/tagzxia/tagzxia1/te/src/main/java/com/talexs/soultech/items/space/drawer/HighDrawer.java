package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.items.space.drawer;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.space.drawer.BaseDrawer;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.internal.RecipeObject;

public class HighDrawer extends BaseDrawer {
    public HighDrawer() {
        super("high", 6140);

        range = 3;
    }

    @Override
    public RecipeObject getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("drawer_middle")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                .addRequired("space_fire_dust")
                .addRequired("virtuals_sensor_terminals_plus")
                ;
    }
}
