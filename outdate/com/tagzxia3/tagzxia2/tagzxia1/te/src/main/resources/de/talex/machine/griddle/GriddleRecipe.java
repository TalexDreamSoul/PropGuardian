package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.resources.de.talex.machine.griddle;

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
public class GriddleRecipe extends RecipeObject {

    private TalexItem need, export;

    private int amount = 1;

    private boolean ironRequired, allowedRepeat;

    private double random = 0.5;

    public GriddleRecipe(String recipeID, SoulTechItem displayItem) {

        super("griddle_recipe_" + recipeID, displayItem);

        displayItem.setRecipe(this);

        this.export = displayItem;

    }

    public GriddleRecipe(String recipeID, TalexItem displayItem) {

        super("griddle_recipe_" + recipeID, displayItem);

        this.export = displayItem;

    }

}
