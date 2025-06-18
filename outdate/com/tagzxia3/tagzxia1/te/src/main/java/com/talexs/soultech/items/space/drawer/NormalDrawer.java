package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.space.drawer;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.space.drawer.BaseDrawer;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.internal.RecipeObject;

public class NormalDrawer extends BaseDrawer {
    public NormalDrawer() {
        super("normal", 640);
    }

    @Override
    public RecipeObject getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired("virtuals_sensor_terminals")
                .addRequired("machine_core")
                .addRequired("virtuals_sensor_terminals")
                .addRequired("machine_core")
                .addRequired("space_dust")
                .addRequired("machine_core")
                .addRequired("virtuals_sensor_terminals")
                .addRequired("machine_core")
                .addRequired("virtuals_sensor_terminals")
                ;
    }
}
