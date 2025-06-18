package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.machine.advanced_workbench;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.item.MineCraftItem;
import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author TalexDreamSoul
 */
public class WorkBenchRecipe extends RecipeObject {

    private final TalexItem[] required = new TalexItem[9];

    @Getter
    private TalexItem export;

    private int requiredPointer = 0;

    @Getter
    private int amount = 1;

    public WorkBenchRecipe(String recipeID, SoulTechItem displayItem) {

        super("workbench_recipe_" + recipeID, displayItem);

        displayItem.setRecipe(this);
        this.setExport(displayItem);

    }

    public WorkBenchRecipe(String recipeID, TalexItem displayItem) {

        super("workbench_recipe_" + recipeID, displayItem);
        this.setExport(displayItem);

    }

    public WorkBenchRecipe setAmount(int amo) {

        this.amount = amo;

        return this;

    }

    public Collection<TalexItem> getRequiredList() {

        return new ArrayList<>(Arrays.asList(this.required));

    }

    public TalexItem getRecipeAsID(int slot) {

        return required[slot - 1];

    }

    public WorkBenchRecipe setExport(TalexItem item) {

        this.export = item;

        return this;

    }

    public WorkBenchRecipe addRequiredNull() {

        return addRequired((String) null);

    }

    public WorkBenchRecipe addRequired(Material material) {

        return addRequired(new MineCraftItem(material));

    }

    public WorkBenchRecipe addRequired(String soulTechItemID) {

        SoulTechItem item = SoulTechItem.get(soulTechItemID);

        return addRequired(item);

    }

    public WorkBenchRecipe addRequired(TalexItem item) {

        if ( requiredPointer < 0 ) {

            requiredPointer = 0;

        }

        if ( requiredPointer >= 9 ) {

            throw new IndexOutOfBoundsException();

        }

        required[requiredPointer] = item;

        requiredPointer++;

        return this;

    }

    public void delRequired(TalexItem item) {

        if ( requiredPointer > 9 ) {
            requiredPointer = 9;
        }

        if ( requiredPointer < 0 ) {
            throw new IndexOutOfBoundsException();
        }

        required[requiredPointer] = null;

        requiredPointer--;

    }

}
