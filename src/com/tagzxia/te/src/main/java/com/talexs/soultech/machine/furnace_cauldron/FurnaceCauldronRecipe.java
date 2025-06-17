package com.tagzxia.te.src.main.java.com.talexs.soultech.machine.furnace_cauldron;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.internal.RecipeObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author TalexDreamSoul
 */
@Getter
@Setter
@Accessors( chain = true )
public class FurnaceCauldronRecipe extends RecipeObject {

    private TalexItem need, export;

    private int amount = 1;

    private long needTime;

    public FurnaceCauldronRecipe(String recipeID, SoulTechItem displayItem, long needTime) {

        super("furnace_cauldron_recipe_" + recipeID, displayItem);

        displayItem.setRecipe(this);

        this.setExport(displayItem);
        this.needTime = needTime;

    }

    public FurnaceCauldronRecipe(String recipeID, TalexItem displayItem, long needTime) {

        super("furnace_cauldron_recipe_" + recipeID, displayItem);

        this.setExport(displayItem);
        this.needTime = needTime;

    }

}
