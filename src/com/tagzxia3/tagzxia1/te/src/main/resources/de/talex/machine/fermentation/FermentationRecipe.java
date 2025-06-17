package com.tagzxia3.tagzxia1.te.src.main.resources.de.talex.machine.fermentation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import pubsher.talexsoultech.talex.guider.category.RecipeObject;
import pubsher.talexsoultech.utils.item.SoulTechItem;
import pubsher.talexsoultech.utils.item.TalexItem;

/**
 * @author TalexDreamSoul
 */
@Getter
@Setter
@Accessors( chain = true )
public class FermentationRecipe extends RecipeObject {

    private TalexItem need, export;

    private int amount = 1;

    private long needTime;

    public FermentationRecipe(String recipeID, SoulTechItem displayItem, long needTime) {

        super("fermentation_recipe_" + recipeID, displayItem);

        displayItem.setRecipe(this);

        this.setExport(displayItem);
        this.needTime = needTime;

    }

    public FermentationRecipe(String recipeID, TalexItem displayItem, long needTime) {

        super("fermentation_cauldron_recipe_" + recipeID, displayItem);

        this.setExport(displayItem);
        this.needTime = needTime;

    }

}
